package com.example.jpa.domain.plan.dto;

import com.example.jpa.domain.plan.entity.TravelPlan;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPlanResponseDto {

    private Long id;
    private String type;
    private String title;
    private Long regionId;
    private String keyword;
    private String difficulty;
    private Integer peopleCount;
    private Integer budgetMin;
    private Integer budgetMax;
    private LocalDate travelDate;
    private Integer durationDays;
    private Integer totalPrice;
    private String status;
    private LocalDateTime createdAt;

    public static TravelPlanResponseDto fromEntity(TravelPlan plan) {
        return TravelPlanResponseDto.builder()
                .id(plan.getId())
                .type(plan.getType())
                .title(plan.getTitle())
                .regionId(plan.getRegionId())
                .keyword(plan.getKeyword())
                .difficulty(plan.getDifficulty())
                .peopleCount(plan.getPeopleCount())
                .budgetMin(plan.getBudgetMin())
                .budgetMax(plan.getBudgetMax())
                .travelDate(plan.getTravelDate())
                .durationDays(plan.getDurationDays())
                .totalPrice(plan.getTotalPrice())
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .build();
    }
}
