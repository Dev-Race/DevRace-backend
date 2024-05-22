package com.sajang.devracebackend.dto.room;

import com.sajang.devracebackend.domain.enums.RoomState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCheckStateResponseDto {

    private RoomState roomState;
}
