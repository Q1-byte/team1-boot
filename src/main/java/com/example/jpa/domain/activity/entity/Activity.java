package com.example.jpa.domain.activity.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(length = 500)
    private String imageUrl;

    @Column(name = "region_id")
    private Long regionId;

    private Double latitude;
    private Double longitude;

    private Integer price;

    @Column(length = 30)
    private String category; // WATER, CULTURE, OUTDOOR, FOOD, CRAFT

    @Column(length = 200)
    private String keywords;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
