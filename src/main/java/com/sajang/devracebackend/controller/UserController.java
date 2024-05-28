package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.dto.user.UserCheckRoomResponseDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.dto.user.UserSolvedResponseDto;
import com.sajang.devracebackend.dto.user.UserUpdateRequestDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PutMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "사용자 프로필 수정 [jwt O]",
            description = """
                - 사진 변경 X : imageFile == null && signupRequestDto.getIsImageChange() == 0
                - 사진 변경 O : imageFile != null && signupRequestDto.getIsImageChange() == 1
                - 기본사진으로 변경 O : imageFile == null && signupRequestDto.getIsImageChange() == 1  \n기본사진 변경시, User의 imageUrl=null로 업데이트
                """)
    public ResponseEntity<ResponseData> updateUserProfile(
            @RequestPart(value="imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value="userUpdateRequestDto") UserUpdateRequestDto userUpdateRequestDto) throws IOException {

        userService.updateUserProfile(imageFile, userUpdateRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_USER);
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
