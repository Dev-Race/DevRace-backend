package com.sajang.devracebackend.domain.room;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.mapping.UserRoom;
import com.sajang.devracebackend.domain.problem.Problem;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "room_name")
    private String roomName;
    @Column(name = "link", unique = true)
    private String link;
    @Column(name = "ranking")
    private String ranking;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "room_state")
    private RoomState roomState;


    @OneToMany(mappedBy = "room")  // Room-UserRoom 양방향매핑
    private List<UserRoom> userRoomList = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;                    // room - problem 단방향 매핑



    @Builder(builderClassName = "RoomSaveBuilder", builderMethodName = "RoomSaveBuilder")
    public Room(String roomName) {
        // 이 빌더는 채팅방 생성때만 사용할 용도
        this.roomName = roomName;
    }
}
