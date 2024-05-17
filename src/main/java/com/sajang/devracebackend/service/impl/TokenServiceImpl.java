package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.Role;
import com.sajang.devracebackend.dto.auth.ReissueRequestDto;
import com.sajang.devracebackend.dto.auth.TokenDto;
import com.sajang.devracebackend.response.exception.exception400.TokenBadRequestException;
import com.sajang.devracebackend.security.jwt.TokenProvider;
import com.sajang.devracebackend.service.TokenService;
import com.sajang.devracebackend.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserService userService;
    private final TokenProvider tokenProvider;


    @Transactional
    @Override
    public TokenDto reissue(ReissueRequestDto reissueRequestDto) {  // Refresh Token으로 Access Token 재발급 메소드

        // RequestDto로 전달받은 Token값들
        String accessToken = reissueRequestDto.getAccessToken();
        String refreshToken = reissueRequestDto.getRefreshToken();

        // Refresh Token 유효성 검사
        if(tokenProvider.validateToken(refreshToken) == false) {
            throw new JwtException("입력한 Refresh Token은 잘못된 토큰입니다.");
        }

        // Access Token에서 userId 가져오기
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        Long userId = Long.valueOf(authentication.getName());

        // userId로 사용자 검색 & 해당 사용자의 role 가져오기
        User user = userService.findUser(userId);
        Role role = user.getRole();

        // DB의 사용자 Refresh Token 값과, 전달받은 Refresh Token의 불일치 여부 검사
        if(!user.getRefreshToken().equals(refreshToken)) {
            throw new TokenBadRequestException("Refresh Token = " + refreshToken);
        }

        TokenDto tokenDto = tokenProvider.generateAccessTokenByRefreshToken(userId, role, refreshToken);
        return tokenDto;
    }

    @Transactional
    @Override
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userService.findUser(userId);
        user.updateRefreshToken(refreshToken);
    }
}
