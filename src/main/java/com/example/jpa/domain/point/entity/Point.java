package com.example.jpa.domain.point.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Long userId;    // 사용자 ID

    @Column(nullable = false)
    private Integer amount;    // 포인트 금액 (양수: 적립, 음수: 사용)

    private String description; // 포인트 내용 설명

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}