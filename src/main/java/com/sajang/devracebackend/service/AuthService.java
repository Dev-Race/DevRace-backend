package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.AuthDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    AuthDto.SignupResponse signup(MultipartFile imageFile, AuthDto.SignupRequest SignupRequestDto) throws IOException;
    void withdrawal();
    AuthDto.TokenResponse reissue(AuthDto.ReissueRequest reissueRequestDto);
    void updateRefreshToken(Long userId, String refreshToken);
}