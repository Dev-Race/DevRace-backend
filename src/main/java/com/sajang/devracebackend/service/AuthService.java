package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.auth.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    AuthDto.SignupResponse signup(MultipartFile imageFile, AuthDto.SignupRequest SignupRequestDto) throws IOException;
    AuthDto.TokenResponse reissue(AuthDto.ReissueRequest reissueRequestDto);
    void withdrawal();
}