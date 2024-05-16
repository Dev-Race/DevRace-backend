package com.sajang.devracebackend.dto.problem;

import com.sajang.devracebackend.domain.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemResponseDto {
    private Integer number;
    private String title;
    private String content;
    private String imageUrl;
    private String problemInput;
    private String problemOutput;
    private String problemLimit;
    private String sampleInput;
    private String sampleOutput;

    @Builder
    public ProblemResponseDto(Problem problem){
        this.number = problem.getNumber();
        this.imageUrl = problem.getImageUrl();
        this.title = problem.getTitle();
        this.content = problem.getContent();
        this.problemInput = problem.getProblemInput();
        this.problemOutput = problem.getProblemOutput();
        this.problemLimit = problem.getProblemLimit();
        this.sampleInput = problem.getSampleInput();
        this.sampleOutput = problem.getSampleOutput();
    }
}
