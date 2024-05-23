package com.sajang.devracebackend.security.jwt;

import com.sajang.devracebackend.domain.enums.Role;
import com.sajang.devracebackend.dto.auth.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 10;  // 10분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 1440 * 14;  // 1440분 x 14 = 24시간 x 14 = 14일 = 2주
    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // 전체 토큰 새로 생성
    public TokenDto generateTokenDto(Long userId, Role role) {
        String accessToken = generateAccessToken(userId, role);
        String refreshToken = generateRefreshToken();

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(parseClaims(accessToken).getExpiration().getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // Access 토큰이 만료된 경우, Refresh Token으로 Access Token 재발급하기 (또는 signup으로 인해 헤더의 jwt 토큰에 등록해둔 권한도 수정해야할때 활용할 것임.)
    public TokenDto generateAccessTokenByRefreshToken(Long userId, Role role, String refreshToken) {
        String accessToken = generateAccessToken(userId, role);

        return TokenDto.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(parseClaims(accessToken).getExpiration().getTime())
                .refreshToken(refreshToken)
                .build();
    }

    // Access Token 생성 후 반환하는 메소드
    public String generateAccessToken(Long userId, Role role) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        String strUserId = String.valueOf(userId);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(strUserId)
                .claim(AUTHORITIES_KEY, role.name())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return accessToken;
    }

    // Refresh Token 생성 후 반환하는 메소드
    public String generateRefreshToken() {
        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        // Refresh Token 생성
        String refreshToken = Jwts.builder()  // 로그인유자(재발급) 용도로써, 중요정보 Claim 없이 만료 시간만 담음.
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return refreshToken;
    }

    // Access Token의 Payload에 저장된 사용자의 아이디와 권한 정보를 토대로 인증하여 Authentication 객체를 만들어 반환하는 메소드
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);  // Access Token의 Payload에 저장된 Claim을 꺼내옴. (JWT 토큰에서 사용자의 아이디와 권한 정보를 획득할 목적)

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");  // 클라이언트가 잘못된 요청을 한 것이 아니라, 서버에서 처리 중에 예기치 않은 에러가 발생한 것이기에, 403이 아닌 500 Error가 적절함.
        }

        // 해당 계정이 갖고있는 권한 목록들을 리턴하는 역할
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);  // 인증 객체 생성.

        return authentication;
    }

    public boolean validateToken(String token) {  // 토큰의 key서명이 일치하고 유효한지 검사하는 메소드 (JWT를 검증하고 처리)
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;  // 서명이 유효하지 않거나 토큰 형식이 잘못된 경우, JwtException 예외 처리가 발생.
    }

    private Claims parseClaims(String accessToken) {  // Access Token의 Payload에 저장된 Claim을 꺼내오는 메소드 (JWT 토큰에서 사용자의 아이디와 권한 정보를 획득할 목적)
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean isExpiredToken(String accessToken) {  // 반환결과가 true면 토큰이 만료됨을 의미.
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
