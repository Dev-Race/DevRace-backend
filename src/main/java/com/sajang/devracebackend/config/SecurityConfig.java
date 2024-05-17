package com.sajang.devracebackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajang.devracebackend.security.jwt.TokenProvider;
import com.sajang.devracebackend.security.jwt.handler.JwtAccessDeniedHandler;
import com.sajang.devracebackend.security.jwt.handler.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> {
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/signup").hasAuthority("ROLE_GUEST")  // '/signup' api는 ROLE_GUEST 권한 로그인 사용자만 사용 가능. 이는 DB 속성값 & 헤더의 jwt 토큰에 등록해둔 권한도 바꾸어 재발급 받아야 한다.

//                            .requestMatchers("/", "/error", "/favicon.ico", "/swagger-ui/**", "/v3/api-docs/**", "/swagger/**", "/health").permitAll()
//                            .requestMatchers("/reissue", "/ws/**").permitAll()
                            .requestMatchers("/**").permitAll()  // 임시 용도

                            .anyRequest().hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");  // permit 지정한 경로들 외에는 전부 USER나 ADMIN 권한이 있어야지 url을 이용 가능하다. (GUEST 불가능)
                })

                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling
                            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                            .accessDeniedHandler(jwtAccessDeniedHandler);
                });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Arrays.asList("*"));  // 임시 용도
//        config.setAllowedOriginPatterns(Arrays.asList("https://www.devrace.site"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
