package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.user.UserCheckRoomResponseDto;
import com.sajang.devracebackend.dto.user.UserSolvedResponseDto;

public interface UserService {
    User findUser(Long userId);
    User findLoginUser();
    UserSolvedResponseDto checkUserSolvedCount();
    UserCheckRoomResponseDto checkCurrentRoom();
}
