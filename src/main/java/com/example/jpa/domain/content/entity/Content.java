package com.example.jpa.domain.content.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body; // 컨텐츠 본문

    private String category; // 가이드, 공지사항 등 구분

    @Builder.Default
    private int viewCount = 0; // 조회수

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regDate;

    // 비즈니스 로직: 컨텐츠 수정
    public void update(String title, String body, String category) {
        this.title = title;
        this.body = body;
        this.category = category;
    }

    // 비즈니스 로직: 조회수 증가
    public void incrementViewCount() {
        this.viewCount++;
    }
}