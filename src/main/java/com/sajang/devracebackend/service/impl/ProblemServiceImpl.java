package com.sajang.devracebackend.service.impl;

import com.sajang.devracebackend.repository.ProblemRepository;
import com.sajang.devracebackend.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    private final ProblemRepository problemRepository;
}
