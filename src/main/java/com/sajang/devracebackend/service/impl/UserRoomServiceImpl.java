package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.MessageType;
import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.RoomDto;
import com.sajang.devracebackend.dto.UserDto;
import com.sajang.devracebackend.dto.UserRoomDto;
import com.sajang.devracebackend.repository.ChatRepository;
import com.sajang.devracebackend.repository.UserRoomBatchRepository;
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
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoomServiceImpl implements UserRoomService {

    private final UserService userService;
    private final RoomService roomService;
    private final UserRoomRepository userRoomRepository;
    private final UserRoomBatchRepository userRoomBatchRepository;
    private final ChatRepository chatRepository;


    @Transactional(readOnly = true)
    @Override
    public UserRoom findUserRoomWithEagerRoom(Long userId, Long roomId, boolean isIncludeUserRoomList, boolean isIncludeProblem) {
        // 서비스 로직에 UserRoom과 내부의 Room 정보를 모두 조회하여 사용하는 경우가 많았기에, 이 코드를 작성함.
        // '@EntityGraph' 또는 'Fetch Join'을 통해 UserRoom 조회시, 내부의 Lazy 로딩으로 선언된 속성 또한 Eager로 한번에 조회하여 N+1 문제를 해결함.

        if(!isIncludeUserRoomList && !isIncludeProblem) {
            // - Eager 조회 : 'UserRoom + UserRoom.room', Lazy 조회 : 'UserRoom.user'
            return findUserRoom(userId, roomId, userRoomRepository::findByUser_IdAndRoom_Id);
        }
        else if(isIncludeUserRoomList && !isIncludeProblem) {
            // - Eager 조회 : 'UserRoom + UserRoom.room + UserRoom.room.userRoomList', Lazy 조회 : 'UserRoom.user'
            return findUserRoom(userId, roomId, userRoomRepository::findByUser_IdAndRoom_IdWithUserRoomList);
        }
        else if(!isIncludeUserRoomList && isIncludeProblem) {
            // - Eager 조회 : 'UserRoom + UserRoom.room + UserRoom.room.problem', Lazy 조회 : 'UserRoom.user'
            return findUserRoom(userId, roomId, userRoomRepository::findByUser_IdAndRoom_IdWithProblem);
        }
        else {  // 잘못된 파라미터 입력
            throw new IllegalArgumentException("메소드에 잘못된 파라미터를 입력하였습니다.");
        }
    }

    @Transactional
    @Override
    public RoomDto.WaitResponse userWaitRoom(RoomDto.WaitRequest waitRequestDto) {
        User user = userService.findUser(waitRequestDto.getUserId());
        Room room = roomService.findRoom(waitRequestDto.getRoomId());

        // 입장
        if(waitRequestDto.getIsManager() == true && waitRequestDto.getIsEnter() == true) {  // 방 동시 입장 시작 (방장만 클릭 가능)
            usersEnterRoom(waitRequestDto.getRoomId());
        }

        // 대기
        room.addWaiting(waitRequestDto.getUserId(), waitRequestDto.getIsManager());
        RoomDto.WaitResponse waitResponseDto = RoomDto.WaitResponse.builder()
                .roomId(waitRequestDto.getRoomId())
                .userId(waitRequestDto.getUserId())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .isManager(waitRequestDto.getIsManager())
                .isEnter(waitRequestDto.getIsEnter())
                .createdTime(LocalDateTime.now())  // 시간을 수동으로 직접 넣어줌.
                .build();

        return waitResponseDto;
    }

    @Transactional
    @Override
    public void userStopWaitRoom(Long roomId) {
        Room room = roomService.findRoom(roomId);
        room.deleteWaiting(SecurityUtil.getCurrentMemberId(), false);  // 대기자 목록에서 해당 사용자 제거.
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

        // userRoomRepository.saveAll(userRoomList);  // JPA의 saveAll()은 기본키가 IDENTITY 전략인 테이블에는 batch 처리가 적용되지않는 문제가 있음.
        userRoomBatchRepository.batchInsert(userRoomList);  // JDBC의 batch insert를 활용하여, 대용량 Batch 저장 처리가 가능함. -> DB 여러번 접근 방지 & 성능 향상

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
    public void solveProblem(Long roomId, UserRoomDto.SolveRequest solveRequestDto) {

        // 'UserRoom.room & UserRoom.userRoomList' Eager 로딩 (N+1 문제 해결)
        UserRoom userRoom = findUserRoomWithEagerRoom(SecurityUtil.getCurrentMemberId(), roomId, true, false);  // 어차피 문제풀이 페이지는 입장 이후이기에, 부모 Room을 갖고있는 자식 UserRoom은 반드시 존재함.
        Room room = userRoom.getRoom();  // 이 시점에는 아직 Fetch Join의 영향을 받지않아, 아직 조회 쿼리가 1번으로 유지됨.

        userRoom.updateLanguage(solveRequestDto.getLanguage());
        userRoom.updateCode(solveRequestDto.getCode());
        userRoom.updateIsLeave(1);  // 더티체킹으로 update가 DB에 바로 반영되지않지만, UserRoom은 동일 트랜잭션 내의 JPA 영속성 컨텍스트가 관리하는 상위 엔티티이므로, 이후 호출되는 하위 'UserRoom.getRoom().getUserRoomList()'에서도 update상태 확인이 가능함.
        userRoom.updateLeaveTime(LocalDateTime.now());

        if(solveRequestDto.getIsRetry() == 0 || (solveRequestDto.getIsRetry() == 1 && solveRequestDto.getIsPass() == 1)) {
            // 재풀이인 경우, 성공인 경우에만 성공여부를 업데이트함. (재풀이의 퇴장인 경우에는 성공여부를 업데이트 하지않음)
            userRoom.updateIsPass(solveRequestDto.getIsPass());
        }

        List<UserRoom> userRoomList = room.getUserRoomList();  // Fetch Join으로 UserRoom 내부의 Room은 Eager 로딩 처리되어, N+1 문제가 발생하지 않음.
        Boolean isLeaveAllUsers = userRoomList.stream()  // Fetch Join으로 UserRoom 내부의 Room.userRoomList는 Eager 로딩 처리되어, N+1 문제가 발생하지 않음.
                .allMatch(enterUserRoom -> enterUserRoom.getIsLeave() == 1);  // 입장했던 모든 유저의 isLeave 값이 1인지 확인 (DB에는 위의 updateIsLeave(1)가 아직 반영되지않았지만, 동일 트랜잭션 내라서 바로 확인이 가능함.)
        if(isLeaveAllUsers == true) room.updateRoomState(RoomState.FINISH);
    }

    @Transactional(readOnly = true)
    @Override
    public UserRoomDto.SolvePageResponse loadSolvePage(Long roomId) {

        // 'UserRoom.room & UserRoom.room.problem' Eager 로딩 (N+1 문제 해결)
        UserRoom userRoom = findUserRoomWithEagerRoom(SecurityUtil.getCurrentMemberId(), roomId, false, true);  // 어차피 문제풀이 페이지는 입장 이후이기에, 부모 Room을 갖고있는 자식 UserRoom은 반드시 존재함.

        List<Long> rankUserIdList = userRoom.getRoom().getWaiting();
        List<UserDto.Response> rankUserDtoList = userService.findUsersOriginal(rankUserIdList, true);

        return new UserRoomDto.SolvePageResponse(userRoom, rankUserDtoList);  // Fetch Join으로 UserRoom 내부의 UserRoom.room과 UserRoom.room.problem은 Eager 로딩 처리되어, N+1 문제가 발생하지 않음.
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserRoomDto.CodePageResponse> findCodeRooms(Integer isPass, Integer number, Pageable pageable) {
        Long loginUserId = SecurityUtil.getCurrentMemberId();
        Page<UserRoom> userRoomPage;

        // 'UserRoom.room & UserRoom.room.problem' Eager 로딩 (N+1 문제 해결)
        if(isPass == null && number == null) {  // 전체 정렬 조회의 경우
            userRoomPage = userRoomRepository.findAllByUser_IdAndIsLeave(loginUserId, 1, pageable);
        }
        else if(isPass != null && number == null) {  // 성공or실패 정렬 조회의 경우
            userRoomPage = userRoomRepository.findAllByUser_IdAndIsLeaveAndIsPass(loginUserId, 1, isPass, pageable);
        }
        else if(isPass == null && number != null) {  // 문제번호 검색 조회의 경우
            userRoomPage = userRoomRepository.findAllByUser_IdAndIsLeaveAndRoom_Problem_Number(loginUserId, 1, number, pageable);
        }
        else {  // 잘못된 URI
            throw new Exception400.UserRoomBadRequest("잘못된 쿼리파라미터로 API를 요청하였습니다.");
        }

        return userRoomPage.map(userRoom -> new UserRoomDto.CodePageResponse(userRoom));
    }

    @Transactional(readOnly = true)
    @Override
    public UserRoomDto.CheckAccessResponse checkAccess(Long roomId) {

        // 'UserRoom.room' Eager 로딩 (N+1 문제 해결)
        Optional<UserRoom> optionalUserRoom = userRoomRepository.findByUser_IdAndRoom_Id(SecurityUtil.getCurrentMemberId(), roomId);

        // UserRoom이 존재하면 해당 정보 사용. 그렇지 않다면 DB에 Room 조회 쿼리 날림.
        Room room = optionalUserRoom
                .map(UserRoom::getRoom)  // 이 시점에는 아직 @EntityGraph의 영향을 받지않아, 아직 조회 쿼리가 1번으로 유지됨.
                .orElseGet(() -> roomService.findRoom(roomId));

        Boolean isExistUserRoom = optionalUserRoom.isPresent();
        Integer isLeave = optionalUserRoom.map(UserRoom::getIsLeave).orElse(null);  // UserRoom이 없다면 isLeave는 null

        UserRoomDto.CheckAccessResponse checkAccessResponseDto = UserRoomDto.CheckAccessResponse.builder()
                .isExistUserRoom(isExistUserRoom)
                .roomState(room.getRoomState())  // @EntityGraph로 UserRoom 내부의 Room은 Eager 로딩 처리되어, N+1 문제가 발생하지 않음.
                .isLeave(isLeave)
                .build();

        return checkAccessResponseDto;
    }


    // ========== 유틸성 메소드 ========== //

    private static UserRoom findUserRoom(Long userId, Long roomId, BiFunction<Long, Long, Optional<UserRoom>> function) {  // userId,roomId,함수를 받아서 UserRoom을 찾는 메소드 (dto가 아닌 entity를 반환하므로, private 접근제어자 사용.)
        return function.apply(userId, roomId).orElseThrow(
                () -> new Exception404.NoSuchUserRoom(String.format("userId = %d & roomId = %d", userId, roomId)));
    }
}
