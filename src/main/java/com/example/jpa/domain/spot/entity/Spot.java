package com.example.jpa.domain.spot.entity;

import com.example.jpa.domain.keyword.entity.SpotKeyword;
import com.example.jpa.domain.region.entity.Region;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private Region region;

    @Column(name = "region_id")
    private Long regionId;

    // ✅ 중복 선언 제거 및 깔끔하게 정리
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer level;

    @Column(unique = true, nullable = false)
    private String apiId;

    @OneToMany(mappedBy = "spot")
    @Builder.Default
    private List<SpotKeyword> spotKeywords = new ArrayList<>();

    public List<String> getKeywordsList() {
        if (this.spotKeywords == null || this.spotKeywords.isEmpty()) {
            return new ArrayList<>();
        }
        return spotKeywords.stream()
                .map(sk -> sk.getKeyword().getName())
                .collect(Collectors.toList());
    }

    private String imageUrl;
    private String address;
    private String category;
    private Double latitude;
    private Double longitude;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public void update(String name, String address, String description, String category,
                       Double latitude, Double longitude, String imageUrl, Integer level,
                       Boolean isActive, Long regionId) {
        if (name != null) this.name = name;
        if (address != null) this.address = address;
        if (description != null) this.description = description;
        if (category != null) this.category = category;
        if (latitude != null) this.latitude = latitude;
        if (longitude != null) this.longitude = longitude;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (level != null) this.level = level;
        if (isActive != null) this.isActive = isActive;
        if (regionId != null) this.regionId = regionId;
    }

    public void toggleActive() {
        this.isActive = !Boolean.TRUE.equals(this.isActive);
    }
}