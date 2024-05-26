package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.chat.ChatResponseDto;
import com.sajang.devracebackend.dto.chat.ChatRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatService {
    ChatResponseDto createChat(ChatRequestDto chatRequestDto);
    Slice<ChatResponseDto> findChatsByRoom(Long roomId, Pageable pageable);
}
