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

    @Column // 소수점이 길어서 정밀도를 높여주는 게 좋습니다
    private Double latitude;

    @Column
    private Double longitude;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(name = "region_id")
    private Long regionId; // 우선 단순 ID로 매핑
    private String address;
    private String category;

    @Column(name = "avg_price")
    private Integer avgPrice;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
