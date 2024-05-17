package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.auth.ReissueRequestDto;
import com.sajang.devracebackend.dto.auth.TokenDto;

public interface TokenService {
    TokenDto reissue(ReissueRequestDto reissueRequestDto);
    void updateRefreshToken(Long userId, String refreshToken);
}
