package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.config.RabbitConfig;
import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomSaveResponseDto;
import com.sajang.devracebackend.dto.room.RoomEnterRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "Room")
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final UserRoomService userRoomService;
    private final RabbitTemplate rabbitTemplate;


    @PostMapping("/rooms")
    @Operation(summary = "방 생성 [jwt O]")
    public ResponseEntity<ResponseData<RoomSaveResponseDto>> createRoom(@RequestBody ProblemSaveRequestDto problemSaveRequestDto) throws IOException {
        RoomSaveResponseDto roomSaveResponseDto = roomService.createRoom(problemSaveRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_ROOM, roomSaveResponseDto);
    }

    @PostMapping("/rooms/{roomId}")
    @Operation(summary = "방 입장 시작 [jwt O]")
    public ResponseEntity<ResponseData> usersEnterRoom(
            @PathVariable(value = "roomId") Long roomId,  // value=""를 작성해주어야만, Swagger에서 api테스트할때 이름값이 뜸.
            @RequestBody RoomEnterRequestDto roomEnterRequestDto) {

        userRoomService.usersEnterRoom(roomId, roomEnterRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_USERROOM);
    }

    @MessageMapping("wait.enter")  // 웹소켓 메시지 처리 (백엔드로 '/pub/wait.enter'를 호출시 이 브로커에서 처리)
    public void userWaitRoom(@Payload RoomWaitRequestDto roomWaitRequestDto) {
        // '/exchange/wait.exchange/waitingroom.{roomId}' 구독되어있는 프론트엔드에게 메세지 전달.
        RoomWaitResponseDto roomWaitResponseDto = roomService.userWaitRoom(roomWaitRequestDto);
        rabbitTemplate.convertAndSend(RabbitConfig.WAIT_EXCHANGE_NAME, "waitingroom." + roomWaitResponseDto.getRoomId(), roomWaitResponseDto);
    }


    // ========== STOMP Test 임시 용도 ========== //
//    @RabbitListener(queues = RabbitConfig.WAIT_QUEUE_NAME)  // 프로듀서(백엔드)->컨슈머(프론트엔드) 과정에서 큐에 도착할 때, 메소드 자동 호출됨.
//    public void receive(RoomWaitResponseDto roomWaitResponseDto){
//        System.out.println("Success Receive - Wait Message / nickname: " + roomWaitResponseDto.getNickname());
//    }
}
