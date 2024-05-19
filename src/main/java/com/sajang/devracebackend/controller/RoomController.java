package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomSaveResponseDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Room")
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;


    @PostMapping("/rooms")
    @Operation(summary = "방 생성 [jwt O]")
    public ResponseEntity<ResponseData<RoomSaveResponseDto>> createRoom(@RequestBody ProblemSaveRequestDto problemSaveRequestDto) throws IOException {
        RoomSaveResponseDto roomSaveResponseDto = roomService.createRoom(problemSaveRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_ROOM, roomSaveResponseDto);
    }
}
