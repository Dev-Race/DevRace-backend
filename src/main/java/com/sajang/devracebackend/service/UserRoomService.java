package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.room.RoomEnterRequestDto;

public interface UserRoomService {
    void createUserRoom(User user, Room room);
    void usersEnterRoom(Long roomId, RoomEnterRequestDto roomEnterRequestDto);
}
