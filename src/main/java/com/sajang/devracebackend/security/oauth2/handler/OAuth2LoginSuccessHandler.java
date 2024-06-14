package com.sajang.devracebackend.security.oauth2.handler;

import com.sajang.devracebackend.domain.enums.Role;
import com.sajang.devracebackend.dto.AuthDto;
import com.sajang.devracebackend.security.jwt.TokenProvider;
import com.sajang.devracebackend.security.oauth2.CustomOAuth2User;
import com.sajang.devracebackend.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthService authService;
    private final TokenProvider tokenProvider;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            Long userId = oAuth2User.getUserId();
            Role role = oAuth2User.getRole();

            AuthDto.TokenResponse tokenResponseDto = tokenProvider.generateTokenDto(userId, role);  // Access & Refresh 토큰 발행.
            String accessToken = tokenResponseDto.getAccessToken();
            log.info("발급된 Access Token : {}", accessToken);
            String refreshToken = tokenResponseDto.getRefreshToken();
            log.info("발급된 Refresh Token : {}", refreshToken);

            // 로그인에 성공했으므로, 사용자 DB에 Refresh Token 저장(있다면 업데이트).
            authService.updateRefreshToken(userId, refreshToken);

            String redirectUrl;
            if(oAuth2User.getRole().equals(Role.ROLE_GUEST)) {  // User의 Role이 GUEST일 경우, 처음 요청한 회원이므로, 회원가입 페이지로 리다이렉트 시켜야함을 프론트에 전달.
                log.info("신규 회원 입니다. JWT 헤더를 가진채로, 추가정보 입력을 위한 회원가입 페이지로 리다이렉트 시킵니다.");  // 리다이렉트(프론트엔드 url)는 백엔드에서 시키고, 헤더에 jwt 다는건 프론트엔드에서.
                String frontendPath = "/info";
                redirectUrl = makeRedirectUrl(tokenResponseDto, frontendPath);
            }
            else {  // 이미 한 번 이상 OAuth2 로그인했던 유저일 때 (즉, 이미 회원가입 추가정보를 입력해두었던 유저일때)
                log.info("기존 회원 입니다. JWT 헤더를 가진채로, 메인 페이지로 리다이렉트 시킵니다.");  // 리다이렉트(프론트엔드 url)는 백엔드에서 시키고, 헤더에 jwt 다는건 프론트엔드에서.
                String frontendPath = "/";
                redirectUrl = makeRedirectUrl(tokenResponseDto, frontendPath);
            }

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);

        } catch (Exception e) {
            throw e;
        }
    }

    public String makeRedirectUrl(AuthDto.TokenResponse tokenResponseDto, String frontendPath) {
        String frontendUrl = "https://www.devrace.site" + frontendPath;

        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)  // 프론트엔드 url
                .queryParam("grantType", tokenResponseDto.getGrantType())
                .queryParam("accessToken", tokenResponseDto.getAccessToken())
                .queryParam("accessTokenExpiresIn", tokenResponseDto.getAccessTokenExpiresIn())
                .queryParam("refreshToken", tokenResponseDto.getRefreshToken())
                .build().toUriString();
        return redirectUrl;
    }
}