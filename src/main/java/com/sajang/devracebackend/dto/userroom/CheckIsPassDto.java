package com.sajang.devracebackend.dto.userroom;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckIsPassDto {

    private String code;
    private Integer isPass;
}
