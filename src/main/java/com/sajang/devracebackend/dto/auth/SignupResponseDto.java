package com.sajang.devracebackend.dto.auth;

import com.sajang.devracebackend.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {

    private UserResponseDto userResponseDto;
    private TokenDto tokenDto;
}
