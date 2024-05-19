package com.sajang.devracebackend.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RoomEnterRequestDto {

    private List<Long> userIdList;
}
