package com.sajang.devracebackend.controller;

import com.sajang.devracebackend.config.RabbitConfig;
import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomResponseDto;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;
import com.sajang.devracebackend.dto.userroom.UserPassRequestDto;
import com.sajang.devracebackend.dto.userroom.RoomCheckAccessResponseDto;
import com.sajang.devracebackend.dto.room.RoomCheckStateResponseDto;
import com.sajang.devracebackend.dto.userroom.SolvingPageResponseDto;
import com.sajang.devracebackend.response.ResponseCode;
import com.sajang.devracebackend.response.ResponseData;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Room")
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final UserRoomService userRoomService;
    private final RabbitTemplate rabbitTemplate;


    @PostMapping("/rooms")
    @Operation(summary = "방생성 Page - 방 생성 [jwt O]")
    public ResponseEntity<ResponseData<RoomResponseDto>> createRoom(@RequestBody ProblemSaveRequestDto problemSaveRequestDto) throws IOException {
        RoomResponseDto roomResponseDto = roomService.createRoom(problemSaveRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.CREATED_ROOM, roomResponseDto);
    }

    @GetMapping("/rooms")
    @Operation(summary = "초대링크 방 조회 [jwt O]", description = "URI : /rooms?link={방링크 string}")
    public ResponseEntity<ResponseData<RoomResponseDto>> findRoomByLink(@RequestParam(value = "link", required = true) String link) {
        RoomResponseDto roomResponseDto = roomService.findRoomByLink(link);
        return ResponseData.toResponseEntity(ResponseCode.READ_ROOM, roomResponseDto);
    }

    @GetMapping("/rooms/{roomId}")
    @Operation(summary = "문제풀이 Page - 문제풀이 페이지 정보 조회 [jwt O]")
    public ResponseEntity<ResponseData<SolvingPageResponseDto>> loadSolvingPage(@PathVariable(value = "roomId") Long roomId) {  // value=""를 작성해주어야만, Swagger에서 api테스트할때 이름값이 뜸.
        SolvingPageResponseDto solvingPageResponseDto = userRoomService.loadSolvingPage(roomId);
        return ResponseData.toResponseEntity(ResponseCode.READ_USERROOM, solvingPageResponseDto);
    }

    @GetMapping("/rooms/{roomId}/access-check")
    @Operation(summary = "문제풀이 Page - 문제풀이 페이지 접근허용 검사 [jwt O]", description = "isLeave == 0 or 1 or null")
    public ResponseEntity<ResponseData<RoomCheckAccessResponseDto>> checkAccess(@PathVariable(value = "roomId") Long roomId) {
        RoomCheckAccessResponseDto roomCheckAccessResponseDto = userRoomService.checkAccess(roomId);
        return ResponseData.toResponseEntity(ResponseCode.READ_USERROOM, roomCheckAccessResponseDto);
    }

    @GetMapping("/rooms/{roomId}/state-check")
    @Operation(summary = "입장 Page - 방 상태 검사 [jwt O]", description = "dtoList index==0 : 방장 / dtoList index==others : 방장 외 대기자들 접속시간 기준 오름차순 정렬")
    public ResponseEntity<ResponseData<RoomCheckStateResponseDto>> checkState(@PathVariable(value = "roomId") Long roomId) {
        RoomCheckStateResponseDto roomCheckStateResponseDto = roomService.checkState(roomId);
        return ResponseData.toResponseEntity(ResponseCode.READ_USERROOM, roomCheckStateResponseDto);
    }

    @PostMapping("/rooms/{roomId}")  // 의미상 PUT보단 POST가 더 적합하다고 판단했음.
    @Operation(summary = "문제풀이 Page - 문제풀이 성공 및 실패/퇴장 [jwt O]", description = "isRetry==0 : 첫풀이 경우 / isRetry==1 : 재풀이 경우")
    public ResponseEntity<ResponseData> passSolvingProblem(
            @PathVariable(value = "roomId") Long roomId,
            @RequestBody UserPassRequestDto userPassRequestDto) {

        userRoomService.passSolvingProblem(roomId, userPassRequestDto);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_USERROOM);  // 비록 POST이지만, 비즈니스 로직은 update임.
    }

    @PutMapping("/rooms/{roomId}")
    @Operation(summary = "입장 Page - 방 입장 대기열 나가기 [jwt O]")
    public ResponseEntity<ResponseData> userStopWaitRoom(@PathVariable(value = "roomId") Long roomId) {
        userRoomService.userStopWaitRoom(roomId);
        return ResponseData.toResponseEntity(ResponseCode.UPDATE_ROOM);
    }

    @MessageMapping("wait.enter")  // 웹소켓 메시지 처리 (백엔드로 '/pub/wait.enter'를 호출시 이 브로커에서 처리)
    public void userWaitRoom(@Payload RoomWaitRequestDto roomWaitRequestDto) {
        // '/exchange/wait.exchange/waitingroom.{roomId}' 구독되어있는 프론트엔드에게 메세지 전달.
        RoomWaitResponseDto roomWaitResponseDto = userRoomService.userWaitRoom(roomWaitRequestDto);
        rabbitTemplate.convertAndSend(RabbitConfig.WAIT_EXCHANGE_NAME, "waitingroom." + roomWaitResponseDto.getRoomId(), roomWaitResponseDto);
    }


    // ========== STOMP Test 임시 용도 ========== //
//    @RabbitListener(queues = RabbitConfig.WAIT_QUEUE_NAME)  // 프로듀서(백엔드)->컨슈머(프론트엔드) 과정에서 큐에 도착할 때, 메소드 자동 호출됨.
//    public void receive(RoomWaitResponseDto roomWaitResponseDto){
//        System.out.println("Success Receive - Wait Message / nickname: " + roomWaitResponseDto.getNickname());
//    }
}
