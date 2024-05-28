package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.user.UserCheckRoomResponseDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.dto.user.UserSolvedResponseDto;
import com.sajang.devracebackend.dto.user.UserUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User findUser(Long userId);
    User findLoginUser();
    <T> List<T> findUsersOriginal(List<Long> userIdList, Boolean isDto);
    UserResponseDto findUserProfile();
    void updateUserProfile(MultipartFile imageFile, UserUpdateRequestDto userUpdateRequestDto) throws IOException;
    UserSolvedResponseDto checkUserSolvedCount();
    UserCheckRoomResponseDto checkCurrentRoom();
}