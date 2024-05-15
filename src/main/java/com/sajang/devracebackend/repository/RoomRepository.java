package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}