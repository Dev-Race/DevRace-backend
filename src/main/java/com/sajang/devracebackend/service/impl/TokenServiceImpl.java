package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
}
