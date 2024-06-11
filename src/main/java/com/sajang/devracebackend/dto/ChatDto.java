package com.sajang.devracebackend.dto;

import com.sajang.devracebackend.domain.Chat;
import com.sajang.devracebackend.domain.enums.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ChatDto {

    // ======== < Request DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class SaveRequest {

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

        public Chat toEntity(String message) {
            return Chat.ChatSaveBuilder()
                    .roomId(roomId)
                    .senderId(senderId)
                    .message(message)
                    .messageType(messageType)
                    .build();
        }
    }


    // ======== < Response DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class Response {

        private Long roomId;
        private Long senderId;
        private String senderName;
        private String senderImageUrl;
        private String message;
        private MessageType messageType;
        private LocalDateTime createdTime;

        public Response(Chat entity, String senderName, String senderImageUrl) {
            this.roomId = entity.getRoomId();
            this.senderId = entity.getSenderId();
            this.senderName = senderName;
            this.senderImageUrl = senderImageUrl;
            this.message = entity.getMessage();
            this.messageType = entity.getMessageType();
            this.createdTime = entity.getCreatedTime();
        }
    }
}
