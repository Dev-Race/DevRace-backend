package com.sajang.devracebackend.dto.chat;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.enums.MessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatResponseDto {

    private Long roomId;
    private Long senderId;
    private String senderName;
    private String senderImageUrl;
    private String message;
    private MessageType messageType;
    private LocalDateTime createdTime;

    public ChatResponseDto(Chat entity) {
        this.roomId = entity.getRoomId();
        this.senderId = entity.getSenderId();
        this.senderName = entity.getSenderName();
        this.senderImageUrl = entity.getSenderImageUrl();
        this.message = entity.getMessage();
        this.messageType = entity.getMessageType();
        this.createdTime = entity.getCreatedTime();
    }
}