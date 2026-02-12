package com.example.jpa.domain.region.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Region {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cityName; // 도시명
    private String areaCode; // 지역코드
    private String sigunguCode;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(name = "level") // DB 컬럼과 매핑
    private Integer level;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}