package com.example.jpa.domain.review.entity;

import com.example.jpa.domain.review.type.ReportStatus;
import com.example.jpa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReviewReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    // [이것만 수정] 단순 Long reporterId 대신 User 객체와 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    // 🚩 [추가] 신고 카테고리 (스팸/광고, 욕설 등)
    private String category;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason; //상세사유

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;


}

