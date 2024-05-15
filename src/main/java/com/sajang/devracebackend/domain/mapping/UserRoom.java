package com.sajang.devracebackend.domain.mapping;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.room.Room;
import com.sajang.devracebackend.domain.user.User;
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
    @Column(name = "is_pass",columnDefinition = "TINYINT default 0")
    private boolean isPass;
    @Column(name = "is_leave",columnDefinition = "TINYINT default 0")
    private boolean isLeave;
    @Column(name = "leave_time")
    private LocalDateTime leaveTime;




    @ManyToOne(fetch = FetchType.LAZY)  // User-UserRoom 양방향매핑
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)  // Room-UserRoom 양방향매핑
    @JoinColumn(name = "room_id")
    private Room room;


    @Builder(builderClassName = "UserRoomSaveBuilder", builderMethodName = "UserRoomSaveBuilder")
    public UserRoom(User user, Room room) {
        // 이 빌더는 UserRoom 생성때만 사용할 용도
        this.user = user;
        this.room = room;
    }
}