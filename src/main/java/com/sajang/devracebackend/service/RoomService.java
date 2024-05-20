package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomSaveResponseDto;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;

import java.io.IOException;

public interface RoomService {
    Room findRoom(Long roomId);
    RoomSaveResponseDto createRoom(ProblemSaveRequestDto problemSaveRequestDto) throws IOException;
    RoomWaitResponseDto userWaitRoom(RoomWaitRequestDto roomWaitRequestDto);
}
