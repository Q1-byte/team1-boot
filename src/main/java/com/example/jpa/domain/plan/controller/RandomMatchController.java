package com.example.jpa.domain.plan.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.plan.dto.RandomMatchRequestDto;
import com.example.jpa.domain.plan.dto.RandomMatchResponseDto;
import com.example.jpa.domain.plan.service.RandomMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/random-match")
public class RandomMatchController {

    private final RandomMatchService randomMatchService;

    /**
     * 랜덤 여행 계획 생성
     * POST /api/random-match?userId=1
     */
    @PostMapping
    public ResponseEntity<ApiResponse<RandomMatchResponseDto>> generate(
            @RequestParam Long userId,
            @RequestBody RandomMatchRequestDto request) {

        RandomMatchResponseDto result = randomMatchService.generateRandomPlan(userId, request);
        return ResponseEntity.ok(ApiResponse.success("랜덤 여행 계획 생성 완료", result));
    }

    /**
     * 다시 랜덤 매칭 (re-random)
     * POST /api/random-match/{planId}/re-random
     */
    @PostMapping("/{planId}/re-random")
    public ResponseEntity<ApiResponse<RandomMatchResponseDto>> reRandom(
            @PathVariable Long planId) {

        RandomMatchResponseDto result = randomMatchService.reRandom(planId);
        return ResponseEntity.ok(ApiResponse.success("다시 랜덤 매칭 완료", result));
    }
}
