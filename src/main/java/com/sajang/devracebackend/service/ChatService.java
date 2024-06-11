package com.sajang.devracebackend.service;

import com.sajang.devracebackend.dto.ChatDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatService {
    Slice<ChatDto.Response> findChatsByRoom(Long roomId, Pageable pageable);
    ChatDto.Response createChat(ChatDto.SaveRequest saveRequestDto);
}
