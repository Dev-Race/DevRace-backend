package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.util.StringListConverter;
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

    @Convert(converter = StringListConverter.class)  // DB에는 String으로 저장됨.
    private List<String> ranking = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "room_state")
    private RoomState roomState;

    @ManyToOne(fetch = FetchType.LAZY)  // Room-Problem 단방향매핑
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @OneToMany(mappedBy = "room")  // Room-UserRoom 양방향매핑
    private List<UserRoom> userRoomList = new ArrayList<>();


    @Builder(builderClassName = "RoomSaveBuilder", builderMethodName = "RoomSaveBuilder")
    public Room(String link, Problem problem) {
        // 이 빌더는 Room 생성때만 사용할 용도
        this.link = link;
        this.ranking = new ArrayList<>();  // 초기값 빈배열인 문자열 -> "__null__"
        this.roomState = RoomState.WAIT;  // 초기값 WAIT
        this.problem = problem;
    }


    public void updateRanking(String nickname) {
        this.ranking.add(nickname);
    }
}