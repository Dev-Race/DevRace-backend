package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 내부의 Lazy 필드를 Eager로 조회하여 N+1 문제 해결 ('@EntityGraph' : 간단한 로직, 'Fetch Join' : 복잡한 로직 or JPA 쿼리메소드와의 네이밍 충돌 방지용)
public interface UserRepository extends JpaRepository<User, Long> {

    // - Eager 조회 : 'User + User.userRoomList'
    // 특징 1 : User가 존재할때 하위의 userRoomList가 비어있더라도 정상 반환되도록, JOIN 대신 LEFT JOIN 사용함.
    // 특징 2 : 비록 OneToMany 필드인 userRoomList를 Eager로 지정하여 카테시안곱의 중복데이터 위험이 있지만, 메소드 반환값이 List가 아닌 고유한 하나의 값이기에, DISTINCT 없이 작성해도 무방함.
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.userRoomList WHERE u.id = :userId")
    Optional<User> findByIdWithEagerUserRoomList(@Param("userId") Long userId);  // 기존의 JPA findById() 메소드를 @Override 하지않고 유지하기위해, @Query로 새로운 메소드 네이밍을 지어주었음. -> '@EntityGraph' 말고 'Fetch Join' 사용.

    // ===================== //

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);  // '소셜 타입, 식별자'로 해당 회원을 찾기 위한 메소드 (차후 추가정보 입력 회원가입 가능)
    boolean existsByBojId(String bojId);
    List<User> findByIdIn(List<Long> userIdList);
}