package com.sajang.devracebackend.dto.userroom;

import com.sajang.devracebackend.domain.enums.Language;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.dto.problem.ProblemResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SolvingPageResponseDto {

    // Problem
    private ProblemResponseDto problemResponseDto;

    // Room
    private List<String> ranking;
    private String link;

    // UserRoom
    private Language language;
    private String code;

    public SolvingPageResponseDto(UserRoom entity) {
        this.problemResponseDto = new ProblemResponseDto(entity.getRoom().getProblem());
        this.ranking = entity.getRoom().getRanking();
        this.link = entity.getRoom().getLink();
        this.language = entity.getLanguage();
        this.code = entity.getCode();
    }
}
