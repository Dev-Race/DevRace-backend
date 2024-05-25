package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;
import com.sajang.devracebackend.dto.userroom.UserPassRequestDto;
import com.sajang.devracebackend.dto.userroom.RoomCheckAccessResponseDto;
import com.sajang.devracebackend.dto.userroom.SolvingPageResponseDto;

public interface UserRoomService {
    void createUserRoom(User user, Room room);
    RoomWaitResponseDto userWaitRoom(RoomWaitRequestDto roomWaitRequestDto);
    void usersEnterRoom(Long roomId);
    void userStopWaitRoom(Long roomId);
    SolvingPageResponseDto loadSolvingPage(Long roomId);
    RoomCheckAccessResponseDto checkAccess(Long roomId);
    void passSolvingProblem(Long roomId, UserPassRequestDto userPassRequestDto);
}
