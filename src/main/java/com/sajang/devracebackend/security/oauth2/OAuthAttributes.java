package com.sajang.devracebackend.security.oauth2;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.Role;
import com.sajang.devracebackend.domain.enums.SocialType;
import com.sajang.devracebackend.security.oauth2.userinfo.GithubOAuth2UserInfo;
import com.sajang.devracebackend.security.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.sajang.devracebackend.security.oauth2.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class OAuthAttributes {  // 소셜별로 데이터를 받는 데이터를 분기 처리하는 클래스

    // 아래 두 필드 모두, 이후에 CustomOAuth2UserService에서 값을 얻어 빌더로 생성 후 반환할 예정.
    private String nameAttributeKey;  // OAuth2 로그인 진행 시 키가 되는 필드 값으로, PK와 같은 의미.
    private OAuth2UserInfo oauth2UserInfo;  // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    // SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환 메소드
    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.GITHUB) {
            return ofGithub(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GithubOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return User.UserSaveBuilder()
                // email과 role도 사용하기 위해 추가하여 빌드.
                .email(UUID.randomUUID() + "@socialUser.com")  // email은 식별 또는 JWT Token을 발급하기 위한 용도뿐이므로, UUID를 사용하여 임의로 랜덤값 설정.
                .role(Role.ROLE_GUEST)  // role은 GUEST로 설정.

                .socialType(socialType)

                // OAuth2UserInfo 데이터
                .socialId(oauth2UserInfo.getId())
                .nickname(oauth2UserInfo.getNickname())
                .imageUrl(oauth2UserInfo.getImageUrl())
                .build();
    }
}
