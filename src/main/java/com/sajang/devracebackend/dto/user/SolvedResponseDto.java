package com.sajang.devracebackend.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SolvedResponseDto {

    private Integer solvedCount;

    @Builder
    public SolvedResponseDto(Integer solvedCount){
        this.solvedCount = solvedCount;
    }
}
