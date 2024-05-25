package com.sajang.devracebackend.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomWaitRequestDto {

    // 방
    private Long roomId;

    // 대기
    private Long userId;  // 대기자 userId
    private Boolean isManager;  // 방장 여부

    // 입장
    private Boolean isEnter;  // 입장 여부
}
