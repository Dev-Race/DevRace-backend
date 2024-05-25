package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.dto.user.UserCheckRoomResponseDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.dto.user.UserSolvedResponseDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/users")
    @Operation(summary = "사용자 프로필 조회 [jwt O]")
    public ResponseEntity<ResponseData<UserResponseDto>> findUserProfile() {
        UserResponseDto userResponseDto = userService.findUserProfile();
        return ResponseData.toResponseEntity(ResponseCode.READ_USER, userResponseDto);
    }

    @GetMapping("/users/solved-count")
    @Operation(summary = "백준 solvedCount값 조회 [jwt O]")
    public ResponseEntity<ResponseData<UserSolvedResponseDto>> checkUserSolvedCount() {
        UserSolvedResponseDto userSolvedResponseDto = userService.checkUserSolvedCount();
        return ResponseData.toResponseEntity(ResponseCode.READ_SOLVEDCOUNT, userSolvedResponseDto);
    }

    @GetMapping("/users/room-check")
    @Operation(summary = "메인 Page - 참여중인 방 여부 검사 [jwt O]", description = "roomId == Long or null")
    public ResponseEntity<ResponseData<UserCheckRoomResponseDto>> checkCurrentRoom() {
        UserCheckRoomResponseDto userCheckRoomResponseDto = userService.checkCurrentRoom();
        return ResponseData.toResponseEntity(ResponseCode.READ_ROOM, userCheckRoomResponseDto);
    }
}
