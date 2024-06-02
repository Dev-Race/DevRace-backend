package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomCheckStateResponseDto;
import com.sajang.devracebackend.dto.room.RoomResponseDto;

import java.io.IOException;

public interface RoomService {
    Room findRoom(Long roomId);
    RoomResponseDto findRoomByLink(String link);
    RoomResponseDto createRoom(ProblemSaveRequestDto problemSaveRequestDto) throws IOException;
    RoomCheckStateResponseDto checkState(Long roomId);
}
