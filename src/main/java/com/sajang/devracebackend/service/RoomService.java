package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.dto.ProblemDto;
import com.sajang.devracebackend.dto.RoomDto;

import java.io.IOException;

public interface RoomService {
    Room findRoom(Long roomId);
    RoomDto.Response findRoomByLink(String link);
    RoomDto.Response createRoom(ProblemDto.SaveRequest saveRequestDto) throws IOException;
    RoomDto.CheckStateResponse checkState(Long roomId);
}
