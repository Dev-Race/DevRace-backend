package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.RoomDto;
import com.sajang.devracebackend.dto.UserRoomDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRoomService {
    UserRoom findUserRoomWithEagerRoom(Long userId, Long roomId, boolean isIncludeUserRoomList, boolean isIncludeProblem);
    RoomDto.WaitResponse userWaitRoom(RoomDto.WaitRequest waitRequestDto);
    void usersEnterRoom(Long roomId);
    void userStopWaitRoom(Long roomId);
    UserRoomDto.SolvePageResponse loadSolvingPage(Long roomId);
    UserRoomDto.CheckAccessResponse checkAccess(Long roomId);
    void passSolvingProblem(Long roomId, UserRoomDto.SolveRequest solveRequest);
    Page<UserRoomDto.CodePageResponse> findCodeRooms(Integer isPass, Integer number, Pageable pageable);
}
