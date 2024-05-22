package com.sajang.devracebackend.dto.userroom;

import com.sajang.devracebackend.domain.enums.RoomState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomCheckAccessResponseDto {

    private Boolean isExistUserRoom;  // 사용자_방 존재 여부
    private RoomState roomState;
    private Integer isLeave;  // 'isExistUserRoom==false'인 경우, null 가능.
}
