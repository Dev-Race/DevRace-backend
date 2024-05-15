package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
}
