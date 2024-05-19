package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.repository.ChatRepository;
import com.sajang.devracebackend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
}
