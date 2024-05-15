package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.domain.room.Room;
import com.sajang.devracebackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {

}