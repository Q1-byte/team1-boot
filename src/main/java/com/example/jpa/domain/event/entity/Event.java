package com.example.jpa.domain.event.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Event{ // 등록/수정일 공통화 권장
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(updatable = false) // 생성시에만 저장
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist // 저장(Insert) 전 실행
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // 수정(Update) 전 실행
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
