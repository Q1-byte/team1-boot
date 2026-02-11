package com.example.jpa.domain.plan.dto;

import com.example.jpa.domain.plan.entity.Plan;
import com.example.jpa.domain.plan.entity.TravelPlan;
import com.example.jpa.domain.plan.entity.TravelSpot;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelPlanResponseDto {

    private Long id;
    private String type;
    private String title;
    private String region;
    private Long regionId;
    private String description;
    private String keyword;
    private List<String> selectedKeywords;
    private String difficulty;
    private Integer peopleCount;
    private Integer budgetMin;
    private Integer budgetMax;
    private LocalDate travelDate;
    private Integer durationDays;
    private Integer totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private Map<String, List<TravelSpot>> schedule;

    /**
     * 1. Plan 엔티티 대응 (추천 서비스용)
     * 빌더 에러 방지를 위해 수동 생성 방식을 사용합니다.
     */
    public static TravelPlanResponseDto fromEntity(Plan plan) {
        if (plan == null) return null;
        TravelPlanResponseDto dto = new TravelPlanResponseDto();
        dto.setId(plan.getId());
        dto.setTitle(plan.getName());
        dto.setRegion(plan.getRegion());
        dto.setDescription(plan.getDescription());
        dto.setCreatedAt(plan.getCreatedAt());
        return dto;
    }

    /**
     * 2. TravelPlan 엔티티 대응 (마이페이지 서비스용)
     * MyPageService의 에러를 해결합니다.
     */
    public static TravelPlanResponseDto fromEntity(TravelPlan plan) {
        if (plan == null) return null;
        TravelPlanResponseDto dto = new TravelPlanResponseDto();
        dto.setId(plan.getId());
        dto.setType(plan.getType());
        dto.setTitle(plan.getTitle());
        dto.setRegionId(plan.getRegionId());
        dto.setKeyword(plan.getKeyword());
        dto.setDifficulty(plan.getDifficulty());
        dto.setPeopleCount(plan.getPeopleCount());
        dto.setBudgetMin(plan.getBudgetMin());
        dto.setBudgetMax(plan.getBudgetMax());
        dto.setTravelDate(plan.getTravelDate());
        dto.setDurationDays(plan.getDurationDays());
        dto.setTotalPrice(plan.getTotalPrice());
        dto.setStatus(plan.getStatus());
        dto.setCreatedAt(plan.getCreatedAt());
        return dto;
    }
}