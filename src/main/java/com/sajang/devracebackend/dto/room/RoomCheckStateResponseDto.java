package com.sajang.devracebackend.dto.room;

import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.dto.user.UserResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCheckStateResponseDto {

    private RoomState roomState;
    private String link;
    private List<UserResponseDto> waitUserDtoList;
}
