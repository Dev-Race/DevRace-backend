package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.util.LongListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor

@Table(name = "room")
@Entity
public class Room extends BaseEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    @Column(name = "link", unique = true)
    private String link;

    @Convert(converter = LongListConverter.class)  // DB에는 String으로 저장됨.
    private List<Long> waiting = new ArrayList<>();  // 0인덱스에는 무조건 방장의 userId가 들어갈것.

    @Convert(converter = LongListConverter.class)  // DB에는 String으로 저장됨.
    private List<Long> ranking = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "room_state")
    private RoomState roomState;

    @ManyToOne(fetch = FetchType.LAZY)  // Room-Problem 단방향매핑
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @OneToMany(mappedBy = "room")  // Room-UserRoom 양방향매핑 (읽기 전용 필드)
    private List<UserRoom> userRoomList = new ArrayList<>();


    @Builder(builderClassName = "RoomSaveBuilder", builderMethodName = "RoomSaveBuilder")
    public Room(String link, Problem problem) {
        // 이 빌더는 Room 생성때만 사용할 용도
        this.link = link;
        this.waiting = new ArrayList<>();  // 초기값 빈배열인 문자열 -> "__null__"
        this.ranking = new ArrayList<>();  // 초기값 빈배열인 문자열 -> "__null__"
        this.roomState = RoomState.WAIT;  // 초기값 WAIT
        this.problem = problem;
    }


    public void addWaiting(Long userId, Boolean isManager) {
        if(isManager == true) {  // 방장인 경우, 리스트의 맨앞에 추가.
            if(this.waiting.contains(userId)) {
                this.waiting.remove(userId);
            }
            this.waiting.add(0, userId);
        }
        else if(!this.waiting.contains(userId)) {  // 방장이 아니며, 리스트에 없는 일반 사용자인 경우
            this.waiting.add(userId);
        }
    }

    public void deleteWaiting(Long userId, Boolean isAllDelete) {
        if(isAllDelete == true) {
            this.waiting.clear();
        }
        else {
            this.waiting.remove(userId);
        }
    }

    public void addRanking(Long userId) {
        this.ranking.add(userId);
    }

    public void updateRoomState(RoomState roomState) {
        this.roomState = roomState;
    }
}