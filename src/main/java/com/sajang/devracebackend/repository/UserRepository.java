package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // '@EntityGraph' 또는 'Fetch Join'을 통해 User 조회시, 내부의 Lazy 로딩으로 선언된 속성 또한 Eager로 한번에 조회.
    // ==> N+1 문제 해결 (간단한 로직은 '@EntityGraph'로, 복잡한 로직이거나 기존 JPA 메소드와의 네이밍 충돌을 방지하고자하는 경우는 'Fetch Join'을 활용.)

    // - Eager 조회 : 'User + User.userRoomList'
    @Query("SELECT u FROM User u JOIN FETCH u.userRoomList WHERE u.id = :userId")
    Optional<User> findByIdWithEagerUserRoomList(@Param("userId") Long userId);  // 기존의 JPA findById() 메소드를 @Override 하지않고 유지하기위해, @Query로 새로운 메소드 네이밍을 지어주었음. -> '@EntityGraph' 말고 'Fetch Join' 사용.

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);  // '소셜 타입, 식별자'로 해당 회원을 찾기 위한 메소드 (차후 추가정보 입력 회원가입 가능)
    boolean existsByBojId(String bojId);
    List<User> findByIdIn(List<Long> userIdList);
}