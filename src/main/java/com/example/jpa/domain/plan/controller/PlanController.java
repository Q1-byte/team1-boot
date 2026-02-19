package com.example.jpa.domain.plan.controller;

import com.example.jpa.domain.history.dto.ViewHistoryResponseDto;
import com.example.jpa.domain.history.service.ViewHistoryService;
import com.example.jpa.domain.plan.dto.SavePlanRequestDto;
import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import com.example.jpa.domain.plan.service.PlanService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans") // 관리자용(admin)과 경로를 분리했습니다.
public class PlanController {

    private final PlanService planService;
    private final ViewHistoryService viewHistoryService;

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

    // AI 추천 일정 저장
    @PostMapping("/save")
    public ResponseEntity<TravelPlanResponseDto> savePlan(
            @RequestBody SavePlanRequestDto request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            userId = request.getUserId();
        }
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(planService.saveTravelPlan(request, userId));
    }

    // 저장된 계획 단건 조회 + 조회 기록 저장
    @GetMapping("/{planId}")
    public ResponseEntity<?> getPlan(
            @PathVariable Long planId,
            @RequestParam(required = false) Long userId,
            HttpSession session) {
        try {
            // 조회 기록 저장 (세션 우선, 없으면 파라미터)
            Long resolvedUserId = (Long) session.getAttribute("userId");
            if (resolvedUserId == null) resolvedUserId = userId;
            if (resolvedUserId != null) {
                viewHistoryService.saveViewHistory(resolvedUserId, planId);
            }
            return ResponseEntity.ok(planService.getTravelPlan(planId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 계획 삭제
    @DeleteMapping("/{planId}")
    public ResponseEntity<String> deletePlan(@PathVariable Long planId) {
        try {
            planService.deleteTravelPlan(planId);
            return ResponseEntity.ok("삭제 완료");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 조회 기록 저장
    @PostMapping("/{planId}/view")
    public ResponseEntity<Void> recordView(@PathVariable Long planId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            viewHistoryService.saveViewHistory(userId, planId);
        }
        return ResponseEntity.ok().build();
    }

    // 최근 본 계획 목록 조회
    @GetMapping("/recent")
    public ResponseEntity<List<ViewHistoryResponseDto>> getRecentPlans(@RequestParam Long userId) {
        return ResponseEntity.ok(viewHistoryService.getRecentViewedPlans(userId));
    }

    // 요청을 받기 위한 간단한 내부 클래스 (또는 별도 DTO 파일로 빼셔도 됩니다)
    @lombok.Getter
    @lombok.Setter
    public static class RecommendationRequest {
        private List<String> keyword; // ["#힐링", "#데이트"]
        private String region;         // "서울"
    }
}