package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.service.TokenService;
import com.sajang.devracebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserService userService;


    @Transactional
    @Override
    public void updateRefreshToken(Long userId, String refreshToken) {
        User user = userService.findUser(userId);
        user.updateRefreshToken(refreshToken);
    }
}
