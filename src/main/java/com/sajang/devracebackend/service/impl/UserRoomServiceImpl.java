package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.user.UserEnterRequestDto;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.repository.UserRoomRepository;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserRoomService;
import com.sajang.devracebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoomServiceImpl implements UserRoomService {

    private final UserService userService;
    private final RoomService roomService;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;


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
        Room room = roomService.findRoom(roomId);
        List<Long> userIdList = userEnterRequestDto.getUserIdList();

        List<User> userList = userRepository.findByIdIn(userIdList);
        List<UserRoom> userRooms = userList.stream()
                .map(user -> UserRoom.UserRoomSaveBuilder()
                        .user(user)
                        .room(room)
                        .build())
                .collect(Collectors.toList());

        // 한번에 저장 (여러번 DB에 접근하는것을 방지.)
        userRoomRepository.saveAll(userRooms);
    }
}
