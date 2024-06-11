package com.sajang.devracebackend.dto;

import com.sajang.devracebackend.domain.Problem;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProblemDto {

    // ======== < Request DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class SaveRequest {

        private Integer problemNumber;  // 백준 문제 번호
    }


    // ======== < Response DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class Response {

        private Integer number;
        private String title;
        private String content;
        private String problemInput;
        private String problemOutput;
        private String problemLimit;
        private List<String> sampleInput;
        private List<String> sampleOutput;

        public Response(Problem entity){
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
}
