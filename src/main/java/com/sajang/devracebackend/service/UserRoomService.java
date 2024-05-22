package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.room.RoomEnterRequestDto;
import com.sajang.devracebackend.dto.userroom.RoomCheckAccessResponseDto;
import com.sajang.devracebackend.dto.room.RoomCheckStateResponseDto;
import com.sajang.devracebackend.dto.userroom.SolvingPageResponseDto;

public interface UserRoomService {
    void createUserRoom(User user, Room room);
    void usersEnterRoom(Long roomId, RoomEnterRequestDto roomEnterRequestDto);
    SolvingPageResponseDto loadSolvingPage(Long roomId);
    RoomCheckAccessResponseDto checkAccess(Long roomId);
}
