package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor

@Table(name = "problem")
@Entity
public class Problem extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long id;

    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "problem_input")
    private String problemInput;

    @Column(name = "problem_output")
    private String problemOutput;

    @Column(name = "problem_limit")
    private String problemLimit;

    @Column(name = "sample_input")
    private String sampleInput;  // 예제 입력

    @Column(name = "sample_output")
    private String sampleOutput;  // 예제 출력

}