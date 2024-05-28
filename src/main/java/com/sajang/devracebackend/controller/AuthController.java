package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.dto.auth.ReissueRequestDto;
import com.sajang.devracebackend.dto.auth.SignupRequestDto;
import com.sajang.devracebackend.dto.auth.SignupResponseDto;
import com.sajang.devracebackend.dto.auth.TokenDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.AuthService;
import com.sajang.devracebackend.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenService tokenService;


    @PutMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입 [jwt O]",
            description = """
                - 사진 변경 X : imageFile == null && signupRequestDto.getIsImageChange() == 0
                - 사진 변경 O : imageFile != null && signupRequestDto.getIsImageChange() == 1
                - 기본사진으로 변경 O : imageFile == null && signupRequestDto.getIsImageChange() == 1  \n기본사진 변경시, User의 imageUrl=null로 업데이트
                """)
    public ResponseEntity<ResponseData<SignupResponseDto>> signup(
            @RequestPart(value="imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value="signupRequestDto") SignupRequestDto signupRequestDto) throws IOException {  // 여기서 Role을 USER로 교체해주지 않으면 다른 로그인 필수 API를 사용하지 못함.

        SignupResponseDto signupResponseDto = authService.signup(imageFile, signupRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_USER, signupResponseDto);  // 이 reponseDto 내에 새로운 JWT Access 토큰이 들어있음. 이후 앞으로는 이걸로 헤더에 장착해야함.
    }

    @PostMapping("/reissue")
    @Operation(summary = "JWT Access Token 재발급(로그인 유지) [jwt X]")
    public ResponseEntity<ResponseData<TokenDto>> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        TokenDto tokenDto = tokenService.reissue(reissueRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.REISSUE_SUCCESS, tokenDto);
    }
}
