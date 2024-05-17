package com.sajang.devracebackend.dto.problem;

import com.sajang.devracebackend.domain.Problem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemDto {

    private String title;
    private String content;
    private String imageUrl;
    private String problemInput;
    private String problemOutput;
    private String problemLimit;
    private String sampleInput;
    private String sampleOutput;
    private Integer number;

    @Builder
    public ProblemDto(String imageUrl, String title, Integer number,String content, String problemInput, String problemOutput, String problemLimit, String sampleInput, String sampleOutput){
        this.imageUrl = imageUrl;
        this.title = title;
        this.number = number;
        this.content =content;
        this.problemInput = problemInput;
        this.problemOutput = problemOutput;
        this.problemLimit = problemLimit;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
    }
    public Problem toEntity(){
        return Problem.builder()
                .imageUrl(imageUrl)
                .title(title)
                .number(number)
                .content(content)
                .problemInput(problemInput)
                .problemOutput(problemOutput)
                .problemLimit(problemLimit)
                .sampleInput(sampleInput)
                .sampleOutput(sampleOutput)
                .build();
    }
    public ProblemDto(Problem problem){
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
