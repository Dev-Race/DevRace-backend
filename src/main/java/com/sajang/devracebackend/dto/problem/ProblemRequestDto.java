package com.sajang.devracebackend.dto.problem;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProblemRequestDto {
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
    public ProblemRequestDto(String imageUrl, String title,Integer number, String content, String problemInput, String problemOutput, String problemLimit, String sampleInput, String sampleOutput){
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
}
