package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    // '@EntityGraph' 또는 'Fetch Join'을 통해 UserRoom 조회시, 내부의 Lazy 로딩으로 선언된 속성 또한 Eager로 한번에 조회.
    // ==> N+1 문제 해결 (간단한 로직은 '@EntityGraph'로, 복잡한 로직이거나 기존 JPA 메소드와의 네이밍 충돌을 방지하고자하는 경우는 'Fetch Join'을 활용.)

    // - Eager 조회 : 'UserRoom + UserRoom.room', Lazy 조회 : 'UserRoom.user'
    @EntityGraph(attributePaths = {"room"})  // 반환값이 List가 아닌 고유한 하나의 값이기에, @EntityGraph임에도 DISTINCT 없이 작성했음.
    Optional<UserRoom> findByUser_IdAndRoom_Id(Long userId, Long roomId);  // 이는 @Query가 아니므로, JPA네이밍 규칙에 영향을 받음.

    // - Eager 조회 : 'UserRoom + UserRoom.room + UserRoom.room.userRoomList', Lazy 조회 : 'UserRoom.user'
    @Query("SELECT ur FROM UserRoom ur JOIN FETCH ur.room r JOIN FETCH r.userRoomList WHERE ur.user.id = :userId AND ur.room.id = :roomId")
    Optional<UserRoom> findByUser_IdAndRoom_IdWithUserRoomList(@Param("userId") Long userId, @Param("roomId") Long roomId);  // @Query 이므로, With문처럼 JPA네이밍 규칙을 지키지않아도됨.

    // - Eager 조회 : 'UserRoom + UserRoom.room + UserRoom.room.problem', Lazy 조회 : 'UserRoom.user'
    @Query("SELECT ur FROM UserRoom ur JOIN FETCH ur.room r JOIN FETCH r.problem WHERE ur.user.id = :userId AND ur.room.id = :roomId")
    Optional<UserRoom> findByUser_IdAndRoom_IdWithProblem(@Param("userId") Long userId, @Param("roomId") Long roomId);  // @Query 이므로, With문처럼 JPA네이밍 규칙을 지키지않아도됨.

    boolean existsByUserAndRoom(User user, Room room);
    Page<UserRoom> findAllByIsLeaveAndUser_Id(Integer isLeave, Long userId, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndIsPass(Integer isLeave, Integer isPass, Pageable pageable);
    Page<UserRoom> findAllByIsLeaveAndRoom_Problem_Number(Integer isLeave, Integer number, Pageable pageable);
    Page<UserRoom> findAllByRoom_Link(String link, Pageable pageable);
}