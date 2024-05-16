package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.dto.solvedcount.SolvedResponseDto;
import com.sajang.devracebackend.repository.UserRepository;
import com.sajang.devracebackend.response.exception.exception404.NoSuchBojIdException;
import com.sajang.devracebackend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public SolvedResponseDto checkUserSolvedCount(String BojId){

        try {
            WebClient webClient = WebClient.builder()
                    .baseUrl("https://solved.ac/api/v3")
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            return webClient.get()
                    .uri("/user/show?handle=" + BojId)
                    .retrieve()
                    .bodyToMono(SolvedResponseDto.class)
                    .block();
        }catch (Exception e){
            throw new NoSuchBojIdException(BojId);
        }
    }
}