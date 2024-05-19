package com.sajang.devracebackend.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserEnterRequestDto {

    private List<Long> userIdList;
}
