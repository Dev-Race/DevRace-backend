package com.sajang.devracebackend.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private String nickname;
    private String bojId;
    private Integer isImageChange;
}
