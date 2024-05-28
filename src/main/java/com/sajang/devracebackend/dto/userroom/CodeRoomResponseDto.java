package com.sajang.devracebackend.dto.userroom;

import com.sajang.devracebackend.domain.enums.Language;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CodeRoomResponseDto {

    // Room
    private Long roomId;
    private Integer number;  // 백준 문제번호 (Room의 Problem)

    // UserRoom
    private Language language;
    private Integer isPass;  // 성공 여부
    private LocalDateTime createdTime;  // 첫 입장 시각

    public CodeRoomResponseDto(UserRoom entity) {
        this.roomId = entity.getRoom().getId();
        this.number = entity.getRoom().getProblem().getNumber();
        this.language = entity.getLanguage();
        this.isPass = entity.getIsPass();
        this.createdTime = entity.getCreatedTime();
    }
}
