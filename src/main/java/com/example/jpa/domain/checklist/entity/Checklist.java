package com.example.jpa.domain.checklist.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "checklist")
public class Checklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String task; // 체크리스트 항목 내용 (예: "프로필 사진 등록하기")

    @Builder.Default
    private boolean mandatory = false; // 필수 항목 여부

    @Builder.Default
    private int priority = 0; // 노출 순서

    // 비즈니스 로직: 내용 수정
    public void update(String task, boolean mandatory, int priority) {
        this.task = task;
        this.mandatory = mandatory;
        this.priority = priority;
    }
}