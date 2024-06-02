package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByLink(String link);
}