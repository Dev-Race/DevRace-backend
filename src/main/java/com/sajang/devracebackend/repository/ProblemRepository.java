package com.sajang.devracebackend.repository;

import com.sajang.devracebackend.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
