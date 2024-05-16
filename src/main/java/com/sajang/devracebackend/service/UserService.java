package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.user.SolvedResponseDto;

public interface UserService {
    User findUser(Long userId);
    SolvedResponseDto getSolvedCount(String bojId);
    SolvedResponseDto checkUserSolvedCount();
}
