package com.sajang.devracebackend.domain;

import com.sajang.devracebackend.domain.common.BaseEntity;
import com.sajang.devracebackend.util.StringListConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @Convert(converter = StringListConverter.class)  // DB에는 String으로 저장됨.
    @Column(name = "sample_input", columnDefinition = "TEXT")
    private List<String> sampleInput = new ArrayList<>();  // 예제 입력

    @Convert(converter = StringListConverter.class)  // DB에는 String으로 저장됨.
    @Column(name = "sample_output", columnDefinition = "TEXT")
    private List<String> sampleOutput = new ArrayList<>();  // 예제 출력


    @Builder(builderClassName = "ProblemSaveBuilder", builderMethodName = "ProblemSaveBuilder")
    public Problem(Integer number, String title, String content, String problemInput, String problemOutput, String problemLimit, List<String> sampleInput, List<String> sampleOutput) {
        // 이 빌더는 Problem 생성때만 사용할 용도
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