package com.sajang.devracebackend.security.oauth2;

import com.sajang.devracebackend.domain.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {  // Resource Server(구글,깃허브 등등)에서 제공하지 않는 추가 정보들을 나의 서비스에서 가지고 있기 위해 생성한 클래스

    private String email;
    private Role role;
    private Long userId;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            String email,
                            Role role,
                            Long userId) {

        super(authorities, attributes, nameAttributeKey);  // 부모인 DefaultOAuth2User 클래스의 생성자를 호출.
        this.email = email;
        this.role = role;
        this.userId = userId;
    }
}