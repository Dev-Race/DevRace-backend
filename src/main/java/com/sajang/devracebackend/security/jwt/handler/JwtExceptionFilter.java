package com.sajang.devracebackend.security.jwt.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ResponseEntity responseEntity = ResponseData.toResponseEntity(ResponseCode.TOKEN_ERROR);
            if(e.getMessage().equals("토큰 만료 - ExpiredJwtException")) {
                responseEntity = ResponseData.toResponseEntity(ResponseCode.TOKEN_EXPIRED);
            }

            // 전체 ResponseEntity 객체를 JSON 문자열로 변환.
            String jsonString = objectMapper.writeValueAsString(responseEntity);
            // 위의 JSON 문자열에서 "body" 필드만 추출.
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode dataNode = rootNode.path("body");
            String jsonData = objectMapper.writeValueAsString(dataNode);

            response.getWriter().write(jsonData);
        }
    }
}