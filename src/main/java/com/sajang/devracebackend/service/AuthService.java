package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.auth.SignupRequestDto;
import com.sajang.devracebackend.dto.auth.SignupResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthService {
    SignupResponseDto signup(MultipartFile imageFile, SignupRequestDto userSignupRequestDto) throws IOException;
}
