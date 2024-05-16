package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.user.SolvedResponseDto;
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

    @Transactional
    @Override
    public SolvedResponseDto getSolvedCount(String bojId){
        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://solved.ac/api/v3")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/user/show?handle=" + bojId)
                    .retrieve()
                    .bodyToMono(SolvedResponseDto.class)
                    .block();
        }catch (Exception e){
            throw new NoSuchBojIdException(bojId);
        }
    }

    @Transactional
    @Override
    public SolvedResponseDto checkUserSolvedCount() {
        Long loginUserId = SecurityUtil.getCurrentMemberId();
        User user = findUser(loginUserId);
        SolvedResponseDto solvedResponseDto = getSolvedCount(user.getBojId());

        return solvedResponseDto;
    }
}