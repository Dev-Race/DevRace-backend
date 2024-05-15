package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.user.SocialType;
import com.sajang.devracebackend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

}