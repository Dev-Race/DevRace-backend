package com.sajang.devracebackend.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_time", updatable = false)  // Role이 GUEST에서 USER로 변환되지않은 상태라도, GUEST로 접근을 한번이라도 했었다면, 그 기준으로 잡았음.
    private LocalDateTime createdTime;

    @LastModifiedDate
    @Column(name = "modified_time")
    private LocalDateTime modifiedTime;
}