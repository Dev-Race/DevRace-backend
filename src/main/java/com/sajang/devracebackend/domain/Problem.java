package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
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

    @Column(name = "number", unique = true)
    private Integer number;  // 백준 문제번호

    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "problem_input", columnDefinition = "TEXT")
    private String problemInput;

    @Column(name = "problem_output", columnDefinition = "TEXT")
    private String problemOutput;

    @Column(name = "problem_limit", columnDefinition = "TEXT")
    private String problemLimit;

    @Column(name = "sample_input", columnDefinition = "TEXT")
    private String sampleInput;  // 예제 입력

    @Column(name = "sample_output", columnDefinition = "TEXT")
    private String sampleOutput;  // 예제 출력


    @Builder
    public Problem(Integer number, String title, String content, String problemInput, String problemOutput, String problemLimit, String sampleInput, String sampleOutput) {
        this.number = number;
        this.title = title;
        this.content = content;
        this.problemInput = problemInput;
        this.problemOutput = problemOutput;
        this.problemLimit = problemLimit;
        this.sampleInput = sampleInput;
        this.sampleOutput = sampleOutput;
    }
}