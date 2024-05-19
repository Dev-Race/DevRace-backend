package com.sajang.devracebackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCurrentRoomResponseDto {

    private Boolean isEnter;  // 참여중인 방 존재 여부
    private Long roomId;  // null 가능.
}
