package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.User;
import com.sajang.devracebackend.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);  // '소셜 타입, 식별자'로 해당 회원을 찾기 위한 메소드 (차후 추가정보 입력 회원가입 가능)
    boolean existsByBojId(String bojId);
    List<User> findByIdIn(List<Long> userIdList);
}