package com.example.jpa.domain.review.entity;

import com.example.jpa.domain.user.entity.User; // [중요] 타 팀원이 만든 User 클래스 임포트
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp; // 팀원 규격 반영
import org.hibernate.annotations.UpdateTimestamp;   // 팀원 규격 반영

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter // 추가: 이제 어디서든 데이터 세팅이 가능합니다.
@NoArgsConstructor
@AllArgsConstructor // 추가: @Builder 에러를 원천 차단합니다.
@Builder // 추가: DTO에서 변환할 때 에러가 나지 않습니다.
@Table(name = "review", indexes = {
        @Index(name = "idx_review_created_at", columnList = "createdAt"),
        @Index(name = "idx_review_view_count", columnList = "viewCount")
})
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Long planId;
    private Integer rating;

    @Builder.Default
    private Boolean isRandom = false;
    @Builder.Default
    private Boolean isPublic = true;
    @Builder.Default
    private Boolean isDeleted = false;
    @Builder.Default
    private Integer viewCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ReviewImage> images = new ArrayList<>();


    //조회수 증가 메서드

    public void increaseViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }


    //리뷰 내용 수정 메서드

    public void update(String title, String content, Integer rating, Boolean isPublic) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.isPublic = isPublic;
    }

    // Review.java 파일 안 적당한 위치에 추가
    public void delete() {
        this.isDeleted = true;
    }
}