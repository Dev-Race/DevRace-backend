package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.dto.UserDto;
import com.sajang.devracebackend.dto.UserRoomDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.UserRoomService;
import com.sajang.devracebackend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "User")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRoomService userRoomService;


    @GetMapping("/users")
    @Operation(summary = "마이 Page - 사용자 프로필 조회 [JWT O]")
    public ResponseEntity<ResponseData<UserDto.Response>> findUserProfile() {
        UserDto.Response userResponseDto = userService.findUserProfile();
        return ResponseData.toResponseEntity(ResponseCode.READ_USER, userResponseDto);
    }

    @PutMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "마이 Page - 사용자 프로필 수정 [JWT O]",
            description = """
                - 사진 변경 X : imageFile == null && signupRequestDto.getIsImageChange() == 0
                - 사진 변경 O : imageFile != null && signupRequestDto.getIsImageChange() == 1
                - 기본사진으로 변경 O : imageFile == null && signupRequestDto.getIsImageChange() == 1  \n기본사진 변경시, User의 imageUrl=null로 업데이트
                """)
    public ResponseEntity<ResponseData> updateUserProfile(
            @RequestPart(value="imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value="userUpdateRequestDto") UserDto.UpdateRequest updateRequestDto) throws IOException {

        userService.updateUserProfile(imageFile, updateRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_USER);
    }

    @GetMapping("/users/rooms")
    @Operation(summary = "내 코드 Page - 코드방 목록 조회/정렬/검색 [JWT O]",
            description = """
                - 전체 정렬 URI : /users/rooms?page={페이지번호 int}
                - 성공or실패 정렬 URI : /users/rooms?isPass={풀이성공여부 int}&page={페이지번호 int}
                - 문제번호 검색 URI : /users/rooms?number={문제번호 int}&page={페이지번호 int}  \nsize, sort 필요X (기본값 : size=9 & sort=createdTime,desc)
                """)
    public ResponseEntity<ResponseData<Page<UserRoomDto.CodePageResponse>>> findCodeRooms(
            @RequestParam(value = "isPass", required = false) Integer isPass,
            @RequestParam(value = "number", required = false) Integer number,
            @PageableDefault(size=9, sort="createdTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<UserRoomDto.CodePageResponse> codePageResponseDtoPage = userRoomService.findCodeRooms(isPass, number, pageable);
        return ResponseData.toResponseEntity(ResponseCode.READ_USERROOM, codePageResponseDtoPage);
    }

    @GetMapping("/users/rooms-check")
    @Operation(summary = "메인 Page - 참여중인 방 여부 검사 [JWT O]", description = "roomId == Long or null")
    public ResponseEntity<ResponseData<UserDto.CheckRoomResponse>> checkCurrentRoom() {
        UserDto.CheckRoomResponse checkRoomResponseDto = userService.checkCurrentRoom();
        return ResponseData.toResponseEntity(ResponseCode.READ_ROOM, checkRoomResponseDto);
    }

    @GetMapping("/users/solved-count")
    @Operation(summary = "사용자 백준 solvedCount값 조회 [JWT O]")
    public ResponseEntity<ResponseData<UserDto.SolvedCountResponse>> checkUserSolvedCount() {
        UserDto.SolvedCountResponse solvedCountResponseDto = userService.checkUserSolvedCount();
        return ResponseData.toResponseEntity(ResponseCode.READ_SOLVEDCOUNT, solvedCountResponseDto);
    }
}
