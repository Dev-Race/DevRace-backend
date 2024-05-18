package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByNumber(Integer number);
}
