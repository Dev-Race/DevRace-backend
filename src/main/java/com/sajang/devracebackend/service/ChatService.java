package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.chat.ChatResponseDto;
import com.sajang.devracebackend.dto.chat.ChatRequestDto;

public interface ChatService {
    ChatResponseDto createChat(ChatRequestDto chatRequestDto);
}
