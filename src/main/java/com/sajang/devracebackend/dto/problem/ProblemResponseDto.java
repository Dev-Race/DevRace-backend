package com.sajang.devracebackend.dto.problem;

import com.sajang.devracebackend.domain.Problem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProblemResponseDto {

    private Integer number;
    private String title;
    private String content;
    private String problemInput;
    private String problemOutput;
    private String problemLimit;
    private List<String> sampleInput;
    private List<String> sampleOutput;

    public ProblemResponseDto(Problem entity){
        this.number = entity.getNumber();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.problemInput = entity.getProblemInput();
        this.problemOutput = entity.getProblemOutput();
        this.problemLimit = entity.getProblemLimit();
        this.sampleInput = entity.getSampleInput();
        this.sampleOutput = entity.getSampleOutput();
    }
}
