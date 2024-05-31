package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.room.RoomWaitRequestDto;
import com.sajang.devracebackend.dto.room.RoomWaitResponseDto;
import com.sajang.devracebackend.dto.userroom.CodeRoomResponseDto;
import com.sajang.devracebackend.dto.userroom.UserPassRequestDto;
import com.sajang.devracebackend.dto.userroom.RoomCheckAccessResponseDto;
import com.sajang.devracebackend.dto.userroom.SolvingPageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRoomService {
    UserRoom findUserRoom(Long userId, Long roomId);  // Eager 조회 : 'UserRoom + 내부 Room', Lazy 조회 : '내부 User'
    RoomWaitResponseDto userWaitRoom(RoomWaitRequestDto roomWaitRequestDto);
    void usersEnterRoom(Long roomId);
    void userStopWaitRoom(Long roomId);
    SolvingPageResponseDto loadSolvingPage(Long roomId);
    RoomCheckAccessResponseDto checkAccess(Long roomId);
    void passSolvingProblem(Long roomId, UserPassRequestDto userPassRequestDto);
    Page<CodeRoomResponseDto> findCodeRoom(Integer isPass, Integer number, String link, Pageable pageable);
}
