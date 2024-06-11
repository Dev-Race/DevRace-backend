package com.sajang.devracebackend.dto.room;

import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class RoomDto {

    // ======== < Request DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class WaitRequest {

        // 방
        private Long roomId;

        // 대기
        private Long userId;  // 대기자 userId
        private Boolean isManager;  // 방장 여부

        // 입장
        private Boolean isEnter;  // 입장 여부
    }


    // ======== < Response DTO > ======== //

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long roomId;
        private RoomState roomState;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaitResponse {

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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckStateResponse {

        private RoomState roomState;
        private String link;
        private List<UserDto.Response> waitUserDtoList;
    }
}
