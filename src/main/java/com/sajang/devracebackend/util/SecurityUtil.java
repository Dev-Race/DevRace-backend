package com.sajang.devracebackend.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() { }

    public static Long getCurrentMemberId() {  // 현재 로그인중인 사용자의 PK userId를 추출하여 반환하는 메소드
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");  // @ExceptionHandler(Exception.class)에서 조건문으로 잡히도록 구성해두었음.
        }

        return Long.parseLong(authentication.getName());
    }
}
