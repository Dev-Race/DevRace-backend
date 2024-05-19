package com.sajang.devracebackend.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomWaitRequestDto {

    private Long roomId;
    private Long userId;  // 대기자 userId
    private Boolean isManager;  // 방장 여부
}
