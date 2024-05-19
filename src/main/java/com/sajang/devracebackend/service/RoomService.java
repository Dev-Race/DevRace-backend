package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomSaveResponseDto;

import java.io.IOException;

public interface RoomService {
    RoomSaveResponseDto createRoom(ProblemSaveRequestDto problemSaveRequestDto) throws IOException;
}
