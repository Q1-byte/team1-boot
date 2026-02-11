package com.example.jpa.domain.accommodation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "accommodation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accommodation {

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

    @Column(name = "price_per_night")
    private Integer pricePerNight;

    @Column(length = 30)
    private String type; // HOTEL, PENSION, GUESTHOUSE, MOTEL, RESORT

    @Column(length = 200)
    private String keywords;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
