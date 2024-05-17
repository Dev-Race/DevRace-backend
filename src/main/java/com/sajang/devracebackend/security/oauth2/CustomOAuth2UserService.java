package com.sajang.devracebackend.security.oauth2;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.SocialType;
import com.sajang.devracebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private static final String GITHUB = "github";  // else: google

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {  // OAuth2 로그인 요청

        // 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서 사용자 정보를 얻고, OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저를 반환.
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // '/oauth2/authorization/google'에서 'google'을 의미.
        SocialType socialType = getSocialType(registrationId);

        // OAuthAttributes의 of메소드 파라미터 속성들
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();  // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes();  // 소셜 로그인에서 API가 제공하는 userInfo의 JSON 값(유저 정보들)

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes); // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        User createdUser = getUser(extractAttributes, socialType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().name())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getRole(),
                createdUser.getId()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if(GITHUB.equals(registrationId)) {
            return SocialType.GITHUB;
        }
        return SocialType.GOOGLE;
    }

    private User getUser(OAuthAttributes attributes, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialId(  // SocialType과 attributes에 들어있는 소셜 로그인의 식별값 id를 통해 회원을 찾음.
                socialType,
                attributes.getOauth2UserInfo().getId()
        ).orElse(null);

        if(findUser == null) {
            return saveUser(attributes, socialType);
        }
        else {
            return findUser;
        }
    }

    private User saveUser(OAuthAttributes attributes, SocialType socialType) {
        User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return userRepository.save(createdUser);  // 생성된 User 객체를 DB에 저장함. (socialType, socialId, email, role 값만 있는 상태)
    }
}