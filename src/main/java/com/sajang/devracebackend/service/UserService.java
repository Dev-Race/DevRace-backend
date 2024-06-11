package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    User findUser(Long userId);
    User findLoginUser();
    <T> List<T> findUsersOriginal(List<Long> userIdList, boolean isDto);
    UserDto.Response findUserProfile();
    void updateUserProfile(MultipartFile imageFile, UserDto.UpdateRequest updateRequestDto) throws IOException;
    UserDto.SolvedCountResponse findUserSolvedCount();
    UserDto.CheckRoomResponse checkRoom();
}