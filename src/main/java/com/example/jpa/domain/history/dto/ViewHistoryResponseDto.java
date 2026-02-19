package com.example.jpa.domain.history.dto;

import com.example.jpa.domain.history.entity.ViewHistory;
import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewHistoryResponseDto {

    private Long id;
    private Long planId;
    private String name;     // plan title
    private String region;   // plan region
    private LocalDateTime viewedAt;
    private TravelPlanResponseDto plan;

    public static ViewHistoryResponseDto fromEntity(ViewHistory history) {
        return ViewHistoryResponseDto.builder()
                .id(history.getId())
                .planId(history.getPlanId())
                .viewedAt(history.getViewedAt())
                .build();
    }

    public static ViewHistoryResponseDto fromEntityWithPlan(ViewHistory history, TravelPlanResponseDto plan) {
        return ViewHistoryResponseDto.builder()
                .id(history.getId())
                .planId(history.getPlanId())
                .name(plan.getTitle())
                .region(plan.getRegion())
                .viewedAt(history.getViewedAt())
                .plan(plan)
                .build();
    }
}
