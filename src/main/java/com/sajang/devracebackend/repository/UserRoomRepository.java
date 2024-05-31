package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {

    // 서비스 로직에 UserRoom과 내부의 Room 정보를 모두 조회하여 사용하는 경우가 많았기에, 이를 적용하였음.
    // @EntityGraph를 통해 UserRoom 조회시, 내부의 Lazy 로딩으로 선언된 Room 또한 Eager로 한번에 조회하여, N+1 문제를 해결.
    // ==> Eager 조회 : 'UserRoom + 내부 Room', Lazy 조회 : '내부 User'
    @EntityGraph(attributePaths = {"room"})
    Optional<UserRoom> findByUser_IdAndRoom_Id(Long userId, Long roomId);

    boolean existsByUserAndRoom(User user, Room room);

    Page<UserRoom> findAllByIsLeaveAndUser_Id(Integer isLeave, Long userId, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndIsPass(Integer isLeave, Integer isPass, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndRoom_Problem_Number(Integer isLeave, Integer number, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndRoom_Link(Integer isLeave, String link, Pageable pageable);
}