package com.sajang.devracebackend.dto.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomWaitResponseDto {

    private Long roomId;
    private Long userId;  // 대기자 userId
    private String nickname;  // 대기자 이름
    private Boolean isManager;  // 방장 여부
}
