package com.example.jpa.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter // 추가: 서비스 로직에서 필드 수정을 위해 필요
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor // 추가: @Builder 사용을 위해 필요
@Builder // 추가: DTO -> Entity 변환을 위해 필요
public class ReviewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private String originName;
    private String storedUrl; // DTO의 storedUrl과 명칭 통일 권장
    private Integer sortOrder;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
