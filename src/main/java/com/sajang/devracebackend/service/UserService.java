package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.user.SolvedResponseDto;

public interface UserService {
    SolvedResponseDto checkUserSolvedCount(String bojId);
}
