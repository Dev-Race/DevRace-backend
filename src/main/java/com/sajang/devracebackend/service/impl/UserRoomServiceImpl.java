package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.repository.UserRoomRepository;
import com.sajang.devracebackend.service.UserRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoomServiceImpl implements UserRoomService {

    private final UserRoomRepository userRoomRepository;
}
