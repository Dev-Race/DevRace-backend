package com.sajang.devracebackend.dto;

import com.sajang.devracebackend.domain.enums.Language;
import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class UserRoomDto {

    // ======== < Request DTO > ======== //

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolveRequest {

        private Integer isRetry;  // 문제 재풀이 여부

        private Language language;
        private String code;
        private Integer isPass;
    }


    // ======== < Response DTO > ======== //

    @Getter
    @NoArgsConstructor
    public static class SolvePageResponse {

        // Problem
        private ProblemDto.Response problemResponseDto;

        // Room
        private List<UserDto.Response> rankUserDtoList;
        private String link;

        // UserRoom
        private Language language;
        private String code;

        public SolvePageResponse(UserRoom entity, List<UserDto.Response> rankUserDtoList) {
            this.problemResponseDto = new ProblemDto.Response(entity.getRoom().getProblem());
            this.rankUserDtoList = rankUserDtoList;
            this.link = entity.getRoom().getLink();
            this.language = entity.getLanguage();
            this.code = entity.getCode();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CodePageResponse {

        // Room
        private Long roomId;
        private Integer number;  // 백준 문제번호 (Room의 Problem)

        // UserRoom
        private Language language;
        private Integer isPass;  // 성공 여부
        private LocalDateTime createdTime;  // 첫 입장 시각

        public CodePageResponse(UserRoom entity) {
            this.roomId = entity.getRoom().getId();
            this.number = entity.getRoom().getProblem().getNumber();
            this.language = entity.getLanguage();
            this.isPass = entity.getIsPass();
            this.createdTime = entity.getCreatedTime();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckAccessResponse {

        private Boolean isExistUserRoom;  // 사용자_방 존재 여부
        private RoomState roomState;
        private Integer isLeave;  // 'isExistUserRoom==false'인 경우, null 가능.
    }
}
