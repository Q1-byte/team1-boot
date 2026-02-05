package com.example.jpa.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "travel_plan")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "template_id")
    private Long templateId;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(length = 100)
    private String title;

    @Column(name = "region_id")
    private Long regionId;

    @Column(length = 100)
    private String keyword;

    @Column(length = 10)
    private String difficulty;

    @Column(name = "event_id")
    private Long eventId;

    @Column(name = "people_count")
    @Builder.Default
    private Integer peopleCount = 1;

    @Column(name = "budget_min")
    private Integer budgetMin;

    @Column(name = "budget_max")
    private Integer budgetMax;

    @Column(name = "travel_date")
    private LocalDate travelDate;

    @Column(name = "duration_days")
    private Integer durationDays;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(length = 20)
    @Builder.Default
    private String status = "READY";

    @Column(name = "re_random_count")
    @Builder.Default
    private Integer reRandomCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
