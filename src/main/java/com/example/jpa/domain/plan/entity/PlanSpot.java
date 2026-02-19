package com.example.jpa.domain.plan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plan_spot")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "spot_id", nullable = false)
    private Long spotId;

    @Column(nullable = false)
    private Integer day;
}
