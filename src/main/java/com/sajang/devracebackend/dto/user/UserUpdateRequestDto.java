package com.sajang.devracebackend.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    private String nickname;
    private Integer isImageChange;  // 1 : 새 사진으로 변경하는 경우. 0 : 기본사진으로 변경하는 경우.
}
