package com.sajang.devracebackend.security.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("OAuth2 소셜 로그인에 실패했습니다. 로그인 방식 선택 페이지로 리다이렉트 시킵니다. => 에러 메시지 : {}", exception.getMessage());

        String redirectUrl = UriComponentsBuilder.fromUriString("https://www.devrace.site/login")  // 프론트엔드 url
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);  // 로그인 방식 선택 페이지로 리다이렉트 시킬것.
    }
}