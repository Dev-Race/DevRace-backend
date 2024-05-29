package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    Optional<UserRoom> findByUserAndRoom(User user, Room room);
    boolean existsByUserAndRoom(User user, Room room);

    Page<UserRoom> findAllByIsLeaveAndUser(Integer isLeave, User user, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndIsPass(Integer isLeave, Integer isPass, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndRoom_Problem_Number(Integer isLeave, Integer number, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndRoom_Link(Integer isLeave, String link, Pageable pageable);
}