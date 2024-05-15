package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.mapping.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {

}