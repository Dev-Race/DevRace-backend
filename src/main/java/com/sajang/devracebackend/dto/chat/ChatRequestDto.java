package com.sajang.devracebackend.dto.chat;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.enums.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRequestDto {

    // cannot NULL
    @NotNull(message = "ERROR - roomId cannot be NULL")
    private Long roomId;
    @NotNull(message = "ERROR - senderId cannot be NULL")
    private Long senderId;  // 첫 입장때는 방장 userId로.
    @NotNull(message = "ERROR - messageType cannot be NULL")
    private MessageType messageType;

    // can NULL
    @Setter
    private String message;  // MessageType == 'ENTER' or 'LEAVE' or 'RANK' 일때는 null 가능. 'TALK'일때는 null 불가능.


    public Chat toEntity(String senderName, String senderImageUrl, String message, LocalDateTime createdTime) {
        return Chat.ChatSaveBuilder()
                .roomId(roomId)
                .senderId(senderId)
                .senderName(senderName)
                .senderImageUrl(senderImageUrl)
                .message(message)
                .messageType(messageType)
                .createdTime(createdTime)
                .build();
    }
}
