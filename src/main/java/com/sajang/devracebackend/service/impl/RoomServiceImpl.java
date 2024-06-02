package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Problem;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.dto.problem.ProblemSaveRequestDto;
import com.sajang.devracebackend.dto.room.RoomCheckStateResponseDto;
import com.sajang.devracebackend.dto.room.RoomResponseDto;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import com.sajang.devracebackend.repository.*;
import com.sajang.devracebackend.response.exception.Exception400;
import com.sajang.devracebackend.response.exception.Exception404;
import com.sajang.devracebackend.service.ProblemService;
import com.sajang.devracebackend.service.RoomService;
import com.sajang.devracebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final UserService userService;
    private final ProblemService problemService;
    private final RoomRepository roomRepository;
    private final ProblemRepository problemRepository;


    @Transactional(readOnly = true)
    @Override
    public Room findRoom(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow(
                () -> new Exception404.NoSuchRoom(String.format("roomId = %d", roomId)));
    }

    @Transactional(readOnly = true)
    @Override
    public RoomResponseDto findRoomByLink(String link) {
        if(link == null) throw new Exception400.RoomBadRequest("방 조회 link==null 에러");

        Room room = roomRepository.findByLink(link).orElseThrow(
                () -> new Exception404.NoSuchRoom("link = " + link));

        return RoomResponseDto.builder()
                .roomId(room.getId())
                .build();
    }

    @Transactional
    @Override
    public RoomResponseDto createRoom(ProblemSaveRequestDto problemSaveRequestDto) throws IOException {
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
        RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                .roomId(roomId)
                .build();

        return roomResponseDto;
    }

    @Transactional(readOnly = true)
    @Override
    public RoomCheckStateResponseDto checkState(Long roomId) {
        Room room = findRoom(roomId);
        List<Long> waitUserIdList = room.getWaiting();
        List<UserResponseDto> waitUserDtoList = userService.findUsersOriginal(waitUserIdList, true);

        RoomCheckStateResponseDto roomCheckStateResponseDto = RoomCheckStateResponseDto.builder()
                .roomState(room.getRoomState())
                .link(room.getLink())
                .waitUserDtoList(waitUserDtoList)
                .build();

        return roomCheckStateResponseDto;
    }
}
