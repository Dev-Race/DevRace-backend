package com.sajang.devracebackend.domain.chat;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
//import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
//@Document(collection = "chat")  // 채팅내역만 MongoDB로 관리.
public class Chat implements Serializable {

    @Id
    private String id;

    private Long roomId;

    private Long senderId;

    private String senderName;

    private String message;

    private MessageType messageType;

    private LocalDateTime createdTime;  // 실시간으로 chat 넘겨주는 시간과 DB에 저장되는 시간을 완전히 같게 하기위해서, 수동으로 직접 현재시각으로 저장하도록 구현하였음.



    @Builder(builderClassName = "ChatSaveBuilder", builderMethodName = "ChatSaveBuilder")
    public Chat(Long roomId, Long senderId, String senderName, String message, MessageType messageType, LocalDateTime createdTime) {
        // 이 빌더는 Chat 생성때만 사용할 용도
        this.roomId = roomId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.messageType = messageType;
        this.createdTime = createdTime;
    }
}