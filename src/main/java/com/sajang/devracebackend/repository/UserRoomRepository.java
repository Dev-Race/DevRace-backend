package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    Optional<UserRoom> findByUserAndRoom(User user, Room room);
    List<UserRoom> findAllByRoom(Room room);
}