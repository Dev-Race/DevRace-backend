package com.sajang.devracebackend.domain.problem;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.domain.room.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity(name = "problem")
@Getter
@NoArgsConstructor
public class Problem extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id; //id
    @Column(name = "image_url")
    private String imageUrl; // 이미지
    @Column(name = "title") // 제목
    private String title;
    @Column(name = "content",columnDefinition = "TEXT")
    private String content; // 문제
    @Column(name = "problem_input")
    private String problemInput; // 입력
    @Column(name = "problem_output")
    private String problemOutput; // 출력
    @Column(name = "problem_limit")
    private String problemLimit; // 제한조건
    @Column(name = "sample_input")
    private String sampleInput; // 예제 입력
    @Column(name = "sample_output")
    private String sampleOutput; // 예제 출력



}