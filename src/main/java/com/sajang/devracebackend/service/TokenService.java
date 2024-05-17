package com.sajang.devracebackend.service;

public interface TokenService {
    void updateRefreshToken(Long userId, String refreshToken);
}
