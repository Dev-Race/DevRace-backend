package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Problem;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomSaveResponseDto;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;
import com.sajang.devracebackend.repository.ProblemRepository;
import com.sajang.devracebackend.repository.RoomRepository;
import com.sajang.devracebackend.response.exception.exception404.NoSuchRoomException;
import com.sajang.devracebackend.service.ProblemService;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final UserService userService;
    private final ProblemService problemService;
    private final ProblemRepository problemRepository;
    private final RoomRepository roomRepository;


    @Transactional(readOnly = true)
    @Override
    public Room findRoom(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(
                ()->new NoSuchRoomException(String.format("roomId = %d", roomId)));
    }

    @Transactional
    @Override
    public RoomSaveResponseDto createRoom(ProblemSaveRequestDto problemSaveRequestDto) throws IOException {
        Integer problemNumber = problemSaveRequestDto.getProblemNumber();

        Problem problem = problemRepository.findByNumber(problemNumber)
                .orElse(null);  // try~catch문 대신 null로 표현했음.
        if(problem == null) {  // DB에 저장된 해당 문제가 없을경우, 크롤링 후 DB에 Problem 저장.
            problem = problemService.crawlAndSaveProblem(problemNumber);
        }

        // 20자리의 UUID link 생성
        String uuid = UUID.randomUUID().toString();
        long l = ByteBuffer.wrap(uuid.getBytes()).getLong();
        String link = Long.toString(l, 9);

        // 방 생성
        Room room = Room.RoomSaveBuilder()
                .link(link)
                .problem(problem)
                .build();
        Long roomId = roomRepository.save(room).getId();
        RoomSaveResponseDto roomSaveResponseDto = RoomSaveResponseDto.builder()
                .roomId(roomId)
                .build();

        return roomSaveResponseDto;
    }

    @Transactional
    @Override
    public RoomWaitResponseDto userWaitRoom(RoomWaitRequestDto roomWaitRequestDto) {
        User user = userService.findUser(roomWaitRequestDto.getUserId());
        RoomWaitResponseDto roomWaitResponseDto = RoomWaitResponseDto.builder()
                .roomId(roomWaitRequestDto.getRoomId())
                .userId(roomWaitRequestDto.getUserId())
                .nickname(user.getNickname())
                .isManager(roomWaitRequestDto.getIsManager())
                .createdTime(LocalDateTime.now())  // 시간을 수동으로 직접 넣어줌.
                .build();

        return roomWaitResponseDto;
    }
}
