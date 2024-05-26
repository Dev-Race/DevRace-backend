package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.user.UserCheckRoomResponseDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.dto.user.UserSolvedResponseDto;

import java.util.List;

public interface UserService {
    User findUser(Long userId);
    User findLoginUser();
    <T> List<T> findUsersOriginal(List<Long> userIdList, Boolean isDto);
    UserResponseDto findUserProfile();
    UserSolvedResponseDto checkUserSolvedCount();
    UserCheckRoomResponseDto checkCurrentRoom();
}
