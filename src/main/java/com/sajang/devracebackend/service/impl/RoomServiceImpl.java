package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.repository.RoomRepository;
import com.sajang.devracebackend.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
}
