package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.user.UserCurrentRoomResponseDto;
import com.sajang.devracebackend.dto.user.UserSolvedResponseDto;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.response.exception.exception404.NoSuchBojIdException;
import com.sajang.devracebackend.response.exception.exception404.NoSuchUserException;
import com.sajang.devracebackend.service.UserService;
import com.sajang.devracebackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    @Override
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()->new NoSuchUserException(String.format("userId = %d", userId)));
    }

    @Transactional(readOnly = true)
    @Override
    public User findLoginUser() {
        Long loginUserId = SecurityUtil.getCurrentMemberId();
        User loginUser = findUser(loginUserId);
        return loginUser;
    }

    @Transactional(readOnly = true)
    @Override
    public UserSolvedResponseDto checkUserSolvedCount() {
        User user = findLoginUser();
        UserSolvedResponseDto userSolvedResponseDto = getSolvedCount(user.getBojId());

        return userSolvedResponseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public UserCurrentRoomResponseDto findCurrentRoom() {
        User user = findLoginUser();

        Optional<UserRoom> optionalUserRoom = user.getUserRoomList().stream()
                .filter(userRoom -> userRoom.getIsLeave() == 0)  // getIsLeave() 값이 0인 경우만 필터링 (참여중인 방 찾기)
                .findFirst();
        UserRoom userRoom = optionalUserRoom.orElse(null);

        UserCurrentRoomResponseDto userCurrentRoomResponseDto;
        if(userRoom == null) {  // 참여중인 방이 없을때 X
            userCurrentRoomResponseDto = UserCurrentRoomResponseDto.builder()
                    .isEnter(false)
                    .roomId(null)
                    .build();
        }
        else {  // 참여중인 방이 있을때 O
            userCurrentRoomResponseDto = UserCurrentRoomResponseDto.builder()
                    .isEnter(true)
                    .roomId(userRoom.getRoom().getId())
                    .build();
        }

        return userCurrentRoomResponseDto;
    }


    // ========== 유틸성 메소드 ========== //

    public static UserSolvedResponseDto getSolvedCount(String bojId){  // WebClient로 외부 solved API 호출 메소드
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://solved.ac/api/v3")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/user/show?handle=" + bojId)
                    .retrieve()
                    .bodyToMono(UserSolvedResponseDto.class)
                    .block();
        }catch (Exception e){
            throw new NoSuchBojIdException("bojId = " + bojId);  // solvedac 서버에 존재하지않는 백준id일경우 예외 처리.
        }
    }
}