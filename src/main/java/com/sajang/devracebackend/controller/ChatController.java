package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.config.RabbitConfig;
import com.sajang.devracebackend.dto.chat.ChatResponseDto;
import com.sajang.devracebackend.dto.chat.ChatRequestDto;
import com.sajang.devracebackend.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;


    @MessageMapping("chat.message")  // 웹소켓 메시지 처리 (백엔드로 '/pub/chat.message'를 호출시 이 브로커에서 처리)
    public void sendMessage(@Payload ChatRequestDto chatRequestDto) {
        // '/exchange/chat.exchange/room.{roomId}' 구독되어있는 프론트엔드에게 메세지 전달.
        ChatResponseDto chatResponseDto = chatService.createChat(chatRequestDto);
        rabbitTemplate.convertAndSend(RabbitConfig.CHAT_EXCHANGE_NAME, "room." + chatResponseDto.getRoomId(), chatResponseDto);
    }
}
