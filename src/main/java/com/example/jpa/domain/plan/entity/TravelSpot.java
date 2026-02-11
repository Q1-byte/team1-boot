package com.example.jpa.domain.plan.entity;

import com.example.jpa.domain.keyword.entity.SpotKeyword;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "travel_spot")
public class TravelSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;        // 장소명
    private String category;
    private String address;     // 주소

    private Double mapx;        // 경도
    private Double mapy;        // 위도

    // 수정 포인트: @Column(name = "image_url")을 제거했습니다.
    // 기본 전략에 의해 자동으로 image_url 컬럼과 매핑됩니다.
    private String imageUrl;

    private String contentid;   // 고유 ID

    @Lob
    private String description; // 상세 설명

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SpotKeyword> spotKeywords = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
}