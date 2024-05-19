package com.sajang.devracebackend.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSolvedResponseDto {

    private Integer solvedCount;

    @Builder
    public UserSolvedResponseDto(Integer solvedCount){
        this.solvedCount = solvedCount;
    }
}
