package com.sajang.devracebackend.security.oauth2.userinfo;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));  // 이때, getId는 Long으로 반환되어 (String)으로 캐스팅될 수 없으므로, String.valueOf()를 사용하여 캐스팅해주었음.
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}