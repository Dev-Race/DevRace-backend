package com.sajang.devracebackend.service;

import com.sajang.devracebackend.domain.Problem;

import java.io.IOException;

public interface ProblemService {
    Problem crawlAndSaveProblem(Integer problemNumber) throws IOException;
}
