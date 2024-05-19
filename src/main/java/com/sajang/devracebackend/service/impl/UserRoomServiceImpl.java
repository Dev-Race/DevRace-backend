package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.MessageType;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.user.UserEnterRequestDto;
import com.sajang.devracebackend.repository.ChatRepository;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.repository.UserRoomRepository;
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
    public void usersEnterRoom(Long roomId, UserEnterRequestDto userEnterRequestDto) {
        User managerUser = userService.findLoginUser();  // 방장 = 로그인된 사용자
        Room room = roomService.findRoom(roomId);
        List<Long> userIdList = userEnterRequestDto.getUserIdList();

        List<User> userList = userRepository.findByIdIn(userIdList);
        List<UserRoom> userRooms = userList.stream()
                .map(user -> UserRoom.UserRoomSaveBuilder()
                        .user(user)
                        .room(room)
                        .build())
                .collect(Collectors.toList());
        userRooms.add(UserRoom.UserRoomSaveBuilder()
                        .user(managerUser)
                        .room(room)
                        .build());

        // 한번에 저장 (여러번 DB에 접근하는것을 방지.)
        userRoomRepository.saveAll(userRooms);

        String message = null;
        if(userList.size() == 0) message = "'" + managerUser.getNickname() + "'님이 방에 참가하였습니다.";
        else message = "'" + managerUser.getNickname() + "'님 외 " + userList.size() + "명이 방에 참가하였습니다.";

        Chat chat = Chat.ChatSaveBuilder()
                .roomId(roomId)
                .senderId(managerUser.getId())
                .senderName(managerUser.getNickname())
                .message(message)
                .messageType(MessageType.ENTER)
                .createdTime(LocalDateTime.now())  // 시간을 수동으로 직접 넣어줌. (실시간으로 chat 넘겨주는 시간과 DB에 저장되는 시간을 완전히 같게 하기위해서임.)
                .build();

        // 입장 채팅 내역 저장
        chatRepository.save(chat);
    }
}
