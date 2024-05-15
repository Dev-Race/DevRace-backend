package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.solvedcount.SolvedResponseDto;

public interface UserService {
    SolvedResponseDto checkUserSolvedCount(String user_id);
}
