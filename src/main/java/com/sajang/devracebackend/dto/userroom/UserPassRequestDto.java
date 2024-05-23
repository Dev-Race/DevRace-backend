package com.sajang.devracebackend.dto.userroom;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPassRequestDto {

    private Integer isRetry;  // 문제 재풀이 여부

    private String code;
    private Integer isPass;
}
