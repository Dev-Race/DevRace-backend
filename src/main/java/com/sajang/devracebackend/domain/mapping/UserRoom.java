package com.sajang.devracebackend.domain.mapping;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.enums.Language;
import com.sajang.devracebackend.domain.Room;
import com.sajang.devracebackend.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

@Table(name = "user_room")
@Entity
public class UserRoom extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_room_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Language language;

    @Column(columnDefinition = "TEXT")
    private String code;

    @Column(name = "is_pass", columnDefinition = "TINYINT(1) default 0", length = 1)
    private Integer isPass;

    @Column(name = "is_leave", columnDefinition = "TINYINT(1) default 0", length = 1)
    private Integer isLeave;

    @Column(name = "leave_time")
    private LocalDateTime leaveTime;  // 퇴장 시각 (반면, createdTime = 입장 시각)

    // Eager 조회의 용도가 적어, 기본 Lazy 조회로만 사용함.
    @ManyToOne(fetch = FetchType.LAZY)  // User-UserRoom 양방향매핑
    @JoinColumn(name = "user_id")
    private User user;

    // UserRoom 조회시 UserRoom.room N+1 문제 해결 ==> Eager 조회 : '@EntityGraph 적용 메소드 활용', Lazy 조회 : '기본 지연로딩 용도'
    @ManyToOne(fetch = FetchType.LAZY)  // Room-UserRoom 양방향매핑
    @JoinColumn(name = "room_id")
    private Room room;


    @Builder(builderClassName = "UserRoomSaveBuilder", builderMethodName = "UserRoomSaveBuilder")
    public UserRoom(User user, Room room) {
        // 이 빌더는 UserRoom 생성때만 사용할 용도
        // code, leaveTime 초기값은 null
        this.language = Language.CPP;  // 초기값 CPP
        this.isPass = 0;
        this.isLeave = 0;  // 차후 내 코드 페이지에서 조회시, 'isLeave == 1'인 데이터를 기준으로 조회해야함.
        this.user = user;
        this.room = room;
    }


    public void updateCode(String code) {
        this.code = code;
    }

    public void updateIsPass(Integer isPass) {
        this.isPass = isPass;
    }

    public void updateIsLeave(Integer isLeave) {
        this.isLeave = isLeave;
    }

    public void updateLeaveTime(LocalDateTime leaveTime) {
        this.leaveTime = leaveTime;
    }
}