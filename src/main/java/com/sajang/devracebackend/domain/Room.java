package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.enums.RoomState;
import com.sajang.devracebackend.domain.mapping.UserRoom;
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

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "link", unique = true)
    private String link;

    private String ranking;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "room_state")
    private RoomState roomState;

    @ManyToOne(fetch = FetchType.LAZY)  // Room-Problem 단방향매핑
    @JoinColumn(name = "problem_id")
    private Problem problem;

    @OneToMany(mappedBy = "room")  // Room-UserRoom 양방향매핑
    private List<UserRoom> userRoomList = new ArrayList<>();

}