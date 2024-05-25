package com.sajang.devracebackend.dto.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomWaitResponseDto {

    // 방
    private Long roomId;

    // 대기
    private Long userId;  // 대기자 userId
    private String nickname;  // 대기자 이름
    private String imageUrl;  // 대기자 이미지url
    private Boolean isManager;  // 방장 여부

    // 입장
    private Boolean isEnter;  // 입장 여부
    private LocalDateTime createdTime;
}
