package com.sajang.devracebackend.security.jwt;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {  // HTTP 요청을 중간에서 가로채어 JWT를 처리하고, 사용자를 인증함으로써 SecurityContextHolder에 해당 인증 정보를 설정하는 역할.

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request);  // 토큰값 문자열 리턴

        if(StringUtils.hasText(jwt) && tokenProvider.isExpiredToken(jwt) == true) {  // 해당 Access Token이 만료되었다면
            throw new JwtException("토큰 만료 - ExpiredJwtException");  // JwtFilter에서 발생한 예외 처리는 ExceptionHandler가 아닌, 앞단의 JwtExceptionFilter에게 던져짐.
        }

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {  // 토큰의 서명이 일치하고 유효한가 (JWT 유효성 검사)
            Authentication authentication = tokenProvider.getAuthentication(jwt);  // 사용자를 인증.
            SecurityContextHolder.getContext().setAuthentication(authentication);  // SecurityContextHolder에 인증 정보를 설정.
        }

        filterChain.doFilter(request, response);
    }

    // 토큰값 문자열 리턴 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);  // 앞부분인 "Bearer "을 제외하여 7인덱스부터 끝까지인 실제 토큰 문자열을 반환.
        }
        return null;
    }
}