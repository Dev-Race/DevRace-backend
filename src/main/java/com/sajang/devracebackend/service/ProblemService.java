package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Problem;
import com.sajang.devracebackend.dto.problem.ProblemDto;

import java.io.IOException;

public interface ProblemService {
    Problem crawlProblem(Integer problemNumber) throws IOException;
}
