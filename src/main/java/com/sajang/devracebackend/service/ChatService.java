package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.ChatDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatService {
    ChatDto.Response createChat(ChatDto.SaveRequest saveRequestDto);
    Slice<ChatDto.Response> findChatsByRoom(Long roomId, Pageable pageable);
}
