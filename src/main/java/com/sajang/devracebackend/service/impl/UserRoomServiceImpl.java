package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.MessageType;
import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.dto.userroom.CodeRoomResponseDto;
import com.sajang.devracebackend.dto.userroom.UserPassRequestDto;
import com.sajang.devracebackend.dto.userroom.RoomCheckAccessResponseDto;
import com.sajang.devracebackend.dto.userroom.SolvingPageResponseDto;
import com.sajang.devracebackend.repository.ChatRepository;
import com.sajang.devracebackend.repository.UserRoomRepository;
import com.sajang.devracebackend.response.exception.Exception400;
import com.sajang.devracebackend.response.exception.Exception404;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserRoomService;
import com.sajang.devracebackend.service.UserService;
import com.sajang.devracebackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoomServiceImpl implements UserRoomService {

    private final UserService userService;
    private final RoomService roomService;
    private final UserRoomRepository userRoomRepository;
    private final ChatRepository chatRepository;


    // 서비스 로직에 UserRoom과 내부의 Room 정보를 모두 조회하여 사용하는 경우가 많았기에, 이를 적용하였음.
    // @EntityGraph를 통해 UserRoom 조회시, 내부의 Lazy 로딩으로 선언된 Room 또한 Eager로 한번에 조회하여, N+1 문제를 해결.
    // ==> Eager 조회 : 'UserRoom + 내부 Room', Lazy 조회 : '내부 User'
    @Transactional(readOnly = true)
    @Override
    public UserRoom findUserRoom(Long userId, Long roomId) {
        return userRoomRepository.findByUser_IdAndRoom_Id(userId, roomId).orElseThrow(
                ()->new Exception404.NoSuchUserRoom(String.format("userId = %d & roomId = %d", userId, roomId)));
    }

    @Transactional
    @Override
    public RoomWaitResponseDto userWaitRoom(RoomWaitRequestDto roomWaitRequestDto) {
        User user = userService.findUser(roomWaitRequestDto.getUserId());
        Room room = roomService.findRoom(roomWaitRequestDto.getRoomId());

        // 입장
        if(roomWaitRequestDto.getIsManager() == true && roomWaitRequestDto.getIsEnter() == true) {  // 방 동시 입장 시작 (방장만 클릭 가능)
            usersEnterRoom(roomWaitRequestDto.getRoomId());
        }

        // 대기
        room.addWaiting(roomWaitRequestDto.getUserId(), roomWaitRequestDto.getIsManager());
        RoomWaitResponseDto roomWaitResponseDto = RoomWaitResponseDto.builder()
                .roomId(roomWaitRequestDto.getRoomId())
                .userId(roomWaitRequestDto.getUserId())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .isManager(roomWaitRequestDto.getIsManager())
                .isEnter(roomWaitRequestDto.getIsEnter())
                .createdTime(LocalDateTime.now())  // 시간을 수동으로 직접 넣어줌.
                .build();

        return roomWaitResponseDto;
    }

    @Transactional
    @Override
    public void usersEnterRoom(Long roomId) {
        Room room = roomService.findRoom(roomId);
        List<Long> waitUserIdList = room.getWaiting();
        List<User> waitUserList = userService.findUsersOriginal(waitUserIdList, false);

        waitUserList = waitUserList.stream()
                .filter(user -> user.getEmail() != null)  // 회원탈퇴한 사용자의 입장을 막는 2중방지 용도
                .collect(Collectors.toList());
        User managerUser = waitUserList.get(0);  // 방장

        List<UserRoom> userRoomList = waitUserList.stream()
                .map(user -> UserRoom.UserRoomSaveBuilder()
                        .user(user)
                        .room(room)
                        .build())
                .collect(Collectors.toList());

        // UserRoom 한번에 저장 (여러번 DB에 접근하는것을 방지.)
        userRoomRepository.saveAll(userRoomList);

        // Room RoomState=START로 수정
        room.updateRoomState(RoomState.START);
        room.deleteWaiting(null, true);  // 대기자 목록 모두 비우기.

        String message = null;
        if(waitUserList.size() == 1) message = "'" + managerUser.getNickname() + "'님이 방에 참가하였습니다.";
        else message = "'" + managerUser.getNickname() + "'님 외 " + (waitUserList.size()-1) + "명이 방에 참가하였습니다.";

        Chat chat = Chat.ChatSaveBuilder()
                .roomId(roomId)
                .senderId(managerUser.getId())
                .message(message)
                .messageType(MessageType.ENTER)
                .build();

        // Chat 입장 채팅 내역 저장
        chatRepository.save(chat);
    }

    @Transactional
    @Override
    public void userStopWaitRoom(Long roomId) {
        Room room = roomService.findRoom(roomId);
        room.deleteWaiting(SecurityUtil.getCurrentMemberId(), false);  // 대기자 목록에서 해당 사용자 제거.
    }

    @Transactional(readOnly = true)
    @Override
    public SolvingPageResponseDto loadSolvingPage(Long roomId) {
        UserRoom userRoom = findUserRoom(SecurityUtil.getCurrentMemberId(), roomId);  // 어차피 문제풀이 페이지는 입장 이후이기에, 부모 Room을 갖고있는 자식 UserRoom은 반드시 존재함.

        List<Long> rankUserIdList = userRoom.getRoom().getWaiting();  // @EntityGraph로 UserRoom 내부의 Room은 Eager 로딩 처리되어, N+1 문제가 발생하지 않음.
        List<UserResponseDto> rankUserDtoList = userService.findUsersOriginal(rankUserIdList, true);

        return new SolvingPageResponseDto(userRoom, rankUserDtoList);  // UserRoom의 Room의 Problem은 호출 빈도수가 낮기도하고, JPA메소드 네이밍이 겹치기에, eager 적용을 하지 않았음.
    }

    @Transactional(readOnly = true)
    @Override
    public RoomCheckAccessResponseDto checkAccess(Long roomId) {
//        System.out.println("========== !!! 메소드 시작 !!! ==========\n");

//        System.out.println("===== UserRoom 조회 =====");
        Optional<UserRoom> optionalUserRoom = userRoomRepository.findByUser_IdAndRoom_Id(SecurityUtil.getCurrentMemberId(), roomId);
//        System.out.println("===== UserRoom 조회 완료. [1번의 쿼리 발생] =====\n");

        // UserRoom이 존재하면 해당 정보 사용. 그렇지 않다면 DB에 Room 조회 쿼리 날림.
//        System.out.println("===== UserRoom.getRoom() 실행 =====");
        Room room = optionalUserRoom
                .map(UserRoom::getRoom)  // 이 시점에는 아직 @EntityGraph의 영향을 받지않아, 아직 조회 쿼리가 1번으로 유지됨.
                .orElseGet(() -> roomService.findRoom(roomId));
//        System.out.println("===== UserRoom의 Room을 가져오지만 Room 내부의 변수는 사용하지않음. [추가쿼리 발생 X] =====\n");

        Boolean isExistUserRoom = optionalUserRoom.isPresent();
//        System.out.println("===== UserRoom.getIsLeave() 실행 =====");
        Integer isLeave = optionalUserRoom.map(UserRoom::getIsLeave).orElse(null);  // UserRoom이 없다면 isLeave는 null
//        System.out.println("===== UserRoom의 Room 외의 타변수를 사용. [추가쿼리 발생 X] =====\n");

//        System.out.println("===== UserRoom.getRoom().getRoomState() 실행 =====");
        RoomCheckAccessResponseDto roomCheckAccessResponseDto = RoomCheckAccessResponseDto.builder()
                .isExistUserRoom(isExistUserRoom)
                .roomState(room.getRoomState())  // @EntityGraph로 UserRoom 내부의 Room은 Eager 로딩 처리되어, N+1 문제가 발생하지 않음.
                .isLeave(isLeave)
                .build();
//        System.out.println("===== UserRoom의 Room 내부의 변수를 사용. [@EntityGraph 미처리시 추가쿼리 발생 O] =====\n");

//        System.out.println("========== !!! 메소드 종료 !!! ==========");

        return roomCheckAccessResponseDto;
    }

    @Transactional
    @Override
    public void passSolvingProblem(Long roomId, UserPassRequestDto userPassRequestDto) {
        UserRoom userRoom = findUserRoom(SecurityUtil.getCurrentMemberId(), roomId);  // 어차피 문제풀이 페이지는 입장 이후이기에, 부모 Room을 갖고있는 자식 UserRoom은 반드시 존재함.
        Room room = userRoom.getRoom();  // 이 시점에는 아직 @EntityGraph의 영향을 받지않아, 아직 조회 쿼리가 1번으로 유지됨.

        userRoom.updateCode(userPassRequestDto.getCode());
        userRoom.updateIsLeave(1);
        userRoom.updateLeaveTime(LocalDateTime.now());

        if(userPassRequestDto.getIsRetry() == 0 || (userPassRequestDto.getIsRetry() == 1 && userPassRequestDto.getIsPass() == 1)) {
            // 재풀이인 경우, 성공인 경우에만 성공여부를 업데이트함. (재풀이의 퇴장인 경우에는 성공여부를 업데이트 하지않음)
            userRoom.updateIsPass(userPassRequestDto.getIsPass());
        }

        List<UserRoom> userRoomList = room.getUserRoomList();  // @EntityGraph로 UserRoom 내부의 Room은 Eager 로딩 처리되어, N+1 문제가 발생하지 않음.
        Boolean isLeaveAllUsers = userRoomList.stream()
                .allMatch(enterUserRoom -> enterUserRoom.getIsLeave() == 1);  // 입장했던 모든 유저의 isLeave 값이 1인지 확인
        if(isLeaveAllUsers == true) room.updateRoomState(RoomState.FINISH);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CodeRoomResponseDto> findCodeRoom(Integer isPass, Integer number, String link, Pageable pageable) {
        Page<UserRoom> userRoomPage;

        if(isPass == null && number == null && link == null) {  // 전체 정렬 조회의 경우
            userRoomPage = userRoomRepository.findAllByIsLeaveAndUser_Id(1, SecurityUtil.getCurrentMemberId(), pageable);
        }
        else if(isPass != null && number == null && link == null) {  // 성공or실패 정렬 조회의 경우
            userRoomPage = userRoomRepository.findAllByIsLeaveAndIsPass(1, isPass, pageable);
        }
        else if(isPass == null && number != null && link == null) {  // 문제번호 검색 조회의 경우
            userRoomPage = userRoomRepository.findAllByIsLeaveAndRoom_Problem_Number(1, number, pageable);
        }
        else if(isPass == null && number == null && link != null) {  // 초대링크 탐색 조회의 경우
            userRoomPage = userRoomRepository.findAllByRoom_Link(link, pageable);
        }
        else {  // 잘못된 URI
            throw new Exception400.UserRoomBadRequest("잘못된 쿼리파라미터로 API를 요청하였습니다.");
        }

        return userRoomPage.map(userRoom -> new CodeRoomResponseDto(userRoom));
    }
}
