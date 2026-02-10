package com.example.jpa.domain.spot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "travel_spot")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Spot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. API 중복 체크를 위한 고유 ID (TourAPI의 contentid)
    @Column(unique = true, nullable = false)
    private String apiId;

    @Column(nullable = false)
    private String name;

    private String description;

    // 2. 이미지 주소를 저장할 필드 추가
    @Column(length = 500) // URL은 길어질 수 있으니 여유 있게 설정
    private String imageUrl;

    @Column(name = "region_id")
    private Long regionId;

    private String address;
    private String category;

    // 정밀도 높은 좌표 관리 (아주 좋습니다!)
    private Double latitude;
    private Double longitude;

    @Column(name = "avg_price")
    private Integer avgPrice;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
