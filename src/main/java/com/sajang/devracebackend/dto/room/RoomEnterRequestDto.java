package com.sajang.devracebackend.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RoomEnterRequestDto {

    private List<Long> userIdList;  // 방장을 제외한 나머지 입장자들 목록
}
