package com.example.jpa.domain.review.entity;

import com.example.jpa.domain.plan.entity.Plan;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer memberId; // 추가 필요!@Column(name = "user_id") // DB 컬럼명이 user_id라면 유지, 아니라면 member_id로 변경
    private Integer spotId;
    private String content;
    private Integer rating;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = true) // 필수값이 아니면 true
    private Plan plan;
}