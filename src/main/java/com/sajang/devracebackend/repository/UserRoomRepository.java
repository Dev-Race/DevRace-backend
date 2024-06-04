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

// 내부의 Lazy 필드를 Eager로 조회하여 N+1 문제 해결 ('@EntityGraph' : 간단한 로직, 'Fetch Join' : 복잡한 로직 or JPA 쿼리메소드와의 네이밍 충돌 방지용)
public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {

    // - Eager 조회 : 'UserRoom + UserRoom.room', Lazy 조회 : 'UserRoom.user'
    // 특징 : OneToMany 필드가 아닌 ManyToOne 필드를 Eager로 지정하였으므로, 카테시안곱의 중복데이터 문제가 발생하지 않음.
    @EntityGraph(attributePaths = {"room"})
    Optional<UserRoom> findByUser_IdAndRoom_Id(Long userId, Long roomId);  // 이는 @Query가 아니므로, JPA네이밍 규칙에 영향을 받음.

    // - Eager 조회 : 'UserRoom + UserRoom.room + UserRoom.room.userRoomList', Lazy 조회 : 'UserRoom.user'
    // 특징 1 : 하위 엔티티의 존재가 없어도 정상 반환해주고 싶다면 LEFT JOIN을 사용해야겠지만, 어차피 하위의 room 및 userRoomList는 반드시 존재하므로, 가능한 한 LEFT JOIN 말고 성능이 더 우수한 JOIN 사용.
    // 특징 2 : 비록 OneToMany 필드인 userRoomList를 Eager로 지정하여 카테시안곱의 중복데이터 위험이 있지만, 메소드 반환값이 List가 아닌 고유한 하나의 값이기에, DISTINCT 없이 작성해도 무방함.
    @Query("SELECT ur FROM UserRoom ur JOIN FETCH ur.room r JOIN FETCH r.userRoomList WHERE ur.user.id = :userId AND ur.room.id = :roomId")
    Optional<UserRoom> findByUser_IdAndRoom_IdWithUserRoomList(@Param("userId") Long userId, @Param("roomId") Long roomId);  // @Query 이므로, With문처럼 JPA네이밍 규칙을 지키지않아도됨.

    // - Eager 조회 : 'UserRoom + UserRoom.room + UserRoom.room.problem', Lazy 조회 : 'UserRoom.user'
    // 특징 : 하위 엔티티의 존재가 없어도 정상 반환해주고 싶다면 LEFT JOIN을 사용해야겠지만, 어차피 하위의 room 및 problem 반드시 존재하므로, 가능한 한 LEFT JOIN 말고 성능이 더 우수한 JOIN 사용.
    @Query("SELECT ur FROM UserRoom ur JOIN FETCH ur.room r JOIN FETCH r.problem WHERE ur.user.id = :userId AND ur.room.id = :roomId")
    Optional<UserRoom> findByUser_IdAndRoom_IdWithProblem(@Param("userId") Long userId, @Param("roomId") Long roomId);  // @Query 이므로, With문처럼 JPA네이밍 규칙을 지키지않아도됨.

    // ===================== //

    // - Eager 조회 : 'Page<UserRoom + UserRoom.room + UserRoom.room.problem>', Lazy 조회 : 'Page<UserRoom.user>'
    // 특징 1-1 : 단일객체가 아닌 다중객체(List,Page..)를 반환하는데 내부 각 객체의 OneToMany 필드를 Eager로 선언하여 조회하는 메소드의 경우, 카테시안곱의 중복데이터 위험이 있음.
    // 특징 1-2 : 하지만 밑의 3가지 메소드에서는 모두, 비록 다중객체를 반환하지만 OneToMany 필드 외의 타속성에만 Eager 조회를 지정하였으므로, '중복 제거 처리' 및 '배치 사이즈 선언'을 하지 않은채로 메소드를 선언하였음.
    @EntityGraph(attributePaths = {"room", "room.problem"})
    Page<UserRoom> findAllByUser_IdAndIsLeave(Long userId, Integer isLeave, Pageable pageable);

    @EntityGraph(attributePaths = {"room", "room.problem"})
    Page<UserRoom> findAllByUser_IdAndIsLeaveAndIsPass(Long userId, Integer isLeave, Integer isPass, Pageable pageable);

    @EntityGraph(attributePaths = {"room", "room.problem"})
    Page<UserRoom> findAllByUser_IdAndIsLeaveAndRoom_Problem_Number(Long userId, Integer isLeave, Integer number, Pageable pageable);

    // ===================== //

    boolean existsByUserAndRoom(User user, Room room);
}