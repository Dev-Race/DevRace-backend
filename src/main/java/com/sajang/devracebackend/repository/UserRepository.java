package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}