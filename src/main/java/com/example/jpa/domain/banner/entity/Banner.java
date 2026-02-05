package com.example.jpa.domain.banner.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "banner")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;      // 배너 제목 (관리용)

    @Column(nullable = false)
    private String imageUrl;   // 이미지 경로

    private String linkUrl;    // 클릭 시 이동할 링크

    private int priority;      // 노출 순서 (낮을수록 먼저 보임)

    @Builder.Default
    private boolean active = true; // 활성화 여부

    // 배너 정보 수정 메서드
    public void changeInfo(String title, String imageUrl, String linkUrl, int priority, boolean active) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.priority = priority;
        this.active = active;
    }
}
