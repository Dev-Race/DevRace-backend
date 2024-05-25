package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.MessageType;
import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;
import com.sajang.devracebackend.dto.userroom.UserPassRequestDto;
import com.sajang.devracebackend.dto.userroom.RoomCheckAccessResponseDto;
import com.sajang.devracebackend.dto.userroom.SolvingPageResponseDto;
import com.sajang.devracebackend.repository.ChatRepository;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.repository.UserRoomRepository;
import com.sajang.devracebackend.response.exception.exception404.NoSuchUserRoomException;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserRoomService;
import com.sajang.devracebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoomServiceImpl implements UserRoomService {

    private final UserService userService;
    private final RoomService roomService;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;
    private final ChatRepository chatRepository;


    @Transactional
    @Override
    public void createUserRoom(User user, Room room) {
        UserRoom userRoom = UserRoom.UserRoomSaveBuilder()
                .user(user)
                .room(room)
                .build();
        userRoomRepository.save(userRoom);
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
        room.addWaiting(roomWaitRequestDto.getRoomId(), roomWaitRequestDto.getIsManager());
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
        List<User> waitUserList = userRepository.findByIdIn(waitUserIdList);
        User managerUser = waitUserList.get(0);  // 방장

        List<UserRoom> userRooms = waitUserList.stream()
                .map(user -> UserRoom.UserRoomSaveBuilder()
                        .user(user)
                        .room(room)
                        .build())
                .collect(Collectors.toList());

        // UserRoom 한번에 저장 (여러번 DB에 접근하는것을 방지.)
        userRoomRepository.saveAll(userRooms);

        // Room RoomState=START로 수정
        room.updateRoomState(RoomState.START);
        room.deleteWaiting(null, true);  // 대기자 목록 모두 비우기.

        String message = null;
        if(waitUserList.size() == 1) message = "'" + managerUser.getNickname() + "'님이 방에 참가하였습니다.";
        else message = "'" + managerUser.getNickname() + "'님 외 " + (waitUserList.size()-1) + "명이 방에 참가하였습니다.";

        Chat chat = Chat.ChatSaveBuilder()
                .roomId(roomId)
                .senderId(managerUser.getId())
                .senderName(managerUser.getNickname())
                .message(message)
                .messageType(MessageType.ENTER)
                .createdTime(LocalDateTime.now())  // 시간을 수동으로 직접 넣어줌. (실시간으로 chat 넘겨주는 시간과 DB에 저장되는 시간을 완전히 같게 하기위해서임.)
                .build();

        // Chat 입장 채팅 내역 저장
        chatRepository.save(chat);
    }

    @Transactional
    @Override
    public void userStopWaitRoom(Long roomId) {
        User loginUser = userService.findLoginUser();
        Room room = roomService.findRoom(roomId);

        room.deleteWaiting(loginUser.getId(), false);
    }

    @Transactional(readOnly = true)
    @Override
    public SolvingPageResponseDto loadSolvingPage(Long roomId) {
        User user = userService.findLoginUser();
        Room room = roomService.findRoom(roomId);
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room).orElseThrow(
                ()->new NoSuchUserRoomException(String.format("userId = %d & roomId = %d", user.getId(), room.getId())));

        return new SolvingPageResponseDto(userRoom);
    }

    @Transactional(readOnly = true)
    @Override
    public RoomCheckAccessResponseDto checkAccess(Long roomId) {
        User user = userService.findLoginUser();
        Room room = roomService.findRoom(roomId);
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room)
                .orElse(null);

        Boolean isExistUserRoom = true;
        if(userRoom == null) isExistUserRoom = false;
        Integer isLeave = null;
        if(userRoom != null) isLeave = userRoom.getIsLeave();

        RoomCheckAccessResponseDto roomCheckAccessResponseDto = RoomCheckAccessResponseDto.builder()
                .isExistUserRoom(isExistUserRoom)
                .roomState(room.getRoomState())
                .isLeave(isLeave)
                .build();

        return roomCheckAccessResponseDto;
    }

    @Transactional
    @Override
    public void passSolvingProblem(Long roomId, UserPassRequestDto userPassRequestDto) {
        User user = userService.findLoginUser();
        Room room = roomService.findRoom(roomId);
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room).orElseThrow(
                ()->new NoSuchUserRoomException(String.format("userId = %d & roomId = %d", user.getId(), room.getId())));

        userRoom.updateCode(userPassRequestDto.getCode());
        userRoom.updateIsLeave(1);
        userRoom.updateLeaveTime(LocalDateTime.now());

        if(userPassRequestDto.getIsRetry() == 0 || (userPassRequestDto.getIsRetry() == 1 && userPassRequestDto.getIsPass() == 1)) {
            // 재풀이인 경우, 성공인 경우에만 성공여부를 업데이트함. (재풀이의 퇴장인 경우에는 성공여부를 업데이트 하지않음)
            userRoom.updateIsPass(userPassRequestDto.getIsPass());
        }

        Boolean isLeaveAllUsers = userRoomRepository.findAllByRoom(room).stream()
                .allMatch(users -> users.getIsLeave() == 1);  // 모든 유저의 isLeave 값이 1인지 확인
        if(isLeaveAllUsers == true) room.updateRoomState(RoomState.FINISH);
    }
}
