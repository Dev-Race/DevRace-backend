package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.domain.Problem;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.dto.problem.ProblemRequestDto;
import com.sajang.devracebackend.dto.room.RoomIdResponseDto;
import com.sajang.devracebackend.repository.ProblemRepository;
import com.sajang.devracebackend.repository.RoomRepository;
import com.sajang.devracebackend.service.ProblemService;
import com.sajang.devracebackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final ProblemService problemService;
    private final ProblemRepository problemRepository;
    private final RoomRepository roomRepository;


    @Transactional
    @Override
    public RoomIdResponseDto createRoom(ProblemRequestDto problemRequestDto) throws IOException {
        Integer problemNumber = problemRequestDto.getProblemNumber();

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
        RoomIdResponseDto roomIdResponseDto = new RoomIdResponseDto(roomId);

        return roomIdResponseDto;
    }
}
