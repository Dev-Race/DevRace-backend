package com.sajang.devracebackend.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private String nickname;
    private String bojId;
    private Integer isImageChange;  // 1 : 새 사진으로 변경하는 경우. 0 : 기본사진으로 변경하는 경우.
}
