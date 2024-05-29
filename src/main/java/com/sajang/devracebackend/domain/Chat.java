package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.enums.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

@Document(collection = "chat")  // 채팅방 내역만 MongoDB로 관리.
public class Chat {

    @Id
    private String id;

    private Long roomId;
    private Long senderId;
    private String message;
    private MessageType messageType;

    @CreatedDate
    private LocalDateTime createdTime;


    @Builder(builderClassName = "ChatSaveBuilder", builderMethodName = "ChatSaveBuilder")
    public Chat(Long roomId, Long senderId, String message, MessageType messageType) {
        // 이 빌더는 Chat 생성때만 사용할 용도
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.messageType = messageType;
    }
}