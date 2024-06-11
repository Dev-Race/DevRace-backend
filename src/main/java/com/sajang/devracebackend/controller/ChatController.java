package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.config.RabbitConfig;
import com.sajang.devracebackend.dto.ChatDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final RabbitTemplate rabbitTemplate;


    @GetMapping("/rooms/{roomId}/chats")
    @Operation(summary = "문제풀이 Page - 채팅 내역 조회 [JWT O]", description = "- URI : /rooms/{roomId}/chats?page={페이지번호 int}  \nsize,sort 필요X (기본값 : size=10 & sort=createdTime,desc)")
    public ResponseEntity<ResponseData<Slice<ChatDto.Response>>> findChatsByRoom(
            @PathVariable(value = "roomId") Long roomId,  // value=""를 작성해주어야만, Swagger에서 api테스트할때 이름값이 뜸.
            @PageableDefault(size=10, sort="createdTime", direction = Sort.Direction.DESC) Pageable pageable) {

        Slice<ChatDto.Response> chatResponseDtoSlice = chatService.findChatsByRoom(roomId, pageable);
        return ResponseData.toResponseEntity(ResponseCode.READ_CHATLIST, chatResponseDtoSlice);
    }

    @MessageMapping("chat.message")  // 웹소켓 메시지 처리 (백엔드로 '/pub/chat.message'를 호출시 이 브로커에서 처리)
    public void sendMessage(@Payload ChatDto.SaveRequest saveRequestDto) {
        // '/exchange/chat.exchange/room.{roomId}' 구독되어있는 프론트엔드에게 메세지 전달.
        ChatDto.Response chatResponseDto = chatService.createChat(saveRequestDto);
        rabbitTemplate.convertAndSend(RabbitConfig.CHAT_EXCHANGE_NAME, "room." + chatResponseDto.getRoomId(), chatResponseDto);
    }


    // ========== STOMP Test 임시 용도 ========== //
//    @RabbitListener(queues = RabbitConfig.CHAT_QUEUE_NAME)  // 프로듀서(백엔드)->컨슈머(프론트엔드) 과정에서 큐에 도착할 때, 메소드 자동 호출됨.
//    public void receive(ChatResponseDto chatResponseDto){
//        System.out.println("Success Receive - Chat Message / nickname: " + chatResponseDto.getSenderName());
//    }
}
