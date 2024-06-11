package com.sajang.devracebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthDto {

    // ======== < Request DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class ReissueRequest {

        private String accessToken;
        private String refreshToken;
    }

    @Getter
    @NoArgsConstructor
    public static class SignupRequest {

        private String nickname;
        private String bojId;
        private Integer isImageChange;
    }


    // ======== < Response DTO > ======== //

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenResponse {

        private String grantType;
        private String accessToken;
        private Long accessTokenExpiresIn;
        private String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignupResponse {

        private UserDto.Response userResponseDto;
        private AuthDto.TokenResponse tokenResponseDto;
    }
}
