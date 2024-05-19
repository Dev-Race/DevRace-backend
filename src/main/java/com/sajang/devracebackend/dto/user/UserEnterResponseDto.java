package com.sajang.devracebackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEnterResponseDto {

    private Boolean isEnter;  // 참여중인 방 존재 여부
    private Long roomId;  // null 가능.
}
