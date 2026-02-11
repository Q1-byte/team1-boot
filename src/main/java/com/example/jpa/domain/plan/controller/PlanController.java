package com.example.jpa.domain.plan.controller;

import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import com.example.jpa.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans") // 관리자용(admin)과 경로를 분리했습니다.
public class PlanController {

    private final PlanService planService;

    /**
     * 사용자가 선택한 키워드와 지역을 바탕으로 2박 3일 일정을 생성합니다.
     * POST /api/plans/recommend
     */
    @PostMapping("/recommend")
    public ResponseEntity<TravelPlanResponseDto> recommendPlan(
            @RequestBody RecommendationRequest request) {

        // Service에서 만든 2박 3일 배분 로직 호출
        TravelPlanResponseDto response = planService.createPlan(
                request.getKeyword(),
                request.getRegion()
        );

        return ResponseEntity.ok(response);
    }

    // 요청을 받기 위한 간단한 내부 클래스 (또는 별도 DTO 파일로 빼셔도 됩니다)
    @lombok.Getter
    @lombok.Setter
    public static class RecommendationRequest {
        private List<String> keyword; // ["#힐링", "#데이트"]
        private String region;         // "서울"
    }
}