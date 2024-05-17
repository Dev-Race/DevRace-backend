package com.sajang.devracebackend.dto.problem;

import com.sajang.devracebackend.domain.Problem;
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

    public ProblemResponseDto(Problem entity){
        this.number = entity.getNumber();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.imageUrl = entity.getImageUrl();
        this.problemInput = entity.getProblemInput();
        this.problemOutput = entity.getProblemOutput();
        this.problemLimit = entity.getProblemLimit();
        this.sampleInput = entity.getSampleInput();
        this.sampleOutput = entity.getSampleOutput();
    }
}
