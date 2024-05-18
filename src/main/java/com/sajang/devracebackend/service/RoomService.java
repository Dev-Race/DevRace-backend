package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.problem.ProblemRequestDto;
import com.sajang.devracebackend.dto.room.RoomIdResponseDto;

import java.io.IOException;

public interface RoomService {
    RoomIdResponseDto createRoom(ProblemRequestDto problemRequestDto) throws IOException;
}
