package com.sajang.devracebackend.dto.user;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserDto {

    // ======== < Request DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class UpdateRequest {

        private String nickname;
        private Integer isImageChange;
    }


    // ======== < Response DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class Response {

        private Long userId;
        private String email;
        private String nickname;
        private String bojId;
        private String imageUrl;
        private SocialType socialType;
        private LocalDateTime createdTime;

        public Response(User entity) {
            this.userId = entity.getId();
            this.email = entity.getEmail();
            this.nickname = entity.getNickname();
            this.bojId = entity.getBojId();
            this.imageUrl = entity.getImageUrl();
            this.socialType = entity.getSocialType();
            this.createdTime = entity.getCreatedTime();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SolvedCountResponse {

        private Integer solvedCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckRoomResponse {

        private Boolean isExistRoom;  // 참여중인 방 존재 여부
        private Long roomId;  // null 가능.
    }

}
