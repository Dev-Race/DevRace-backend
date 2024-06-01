package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface ChatRepository extends MongoRepository<Chat, String> {  // MongoDB
    Slice<Chat> findAllByRoomId(Long roomId, Pageable pageable);
    Slice<Chat> findAllByRoomIdAndCreatedTimeLessThanEqual(Long roomId, LocalDateTime leaveTime, Pageable pageable);
}