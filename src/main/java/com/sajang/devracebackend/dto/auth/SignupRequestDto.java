package com.sajang.devracebackend.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private String nickname;
    private String bojId;
    private boolean isImageChange;  // true : 새 사진으로 변경하는 경우. false : 기본사진으로 변경하는 경우.
}
