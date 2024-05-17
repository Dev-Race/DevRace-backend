package com.sajang.devracebackend.dto.user;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.SocialType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String bojId;
    private String imageUrl;
    private SocialType socialType;
    private LocalDateTime createdTime;

    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.bojId = entity.getBojId();
        this.imageUrl = entity.getImageUrl();
        this.socialType = entity.getSocialType();
        this.createdTime = entity.getCreatedTime();
    }
}
