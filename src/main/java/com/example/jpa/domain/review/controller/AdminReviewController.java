package com.example.jpa.domain.review.controller;

import com.example.jpa.domain.review.dto.ReviewResponseDto;
import com.example.jpa.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    // 전체 리뷰 목록 조회 (삭제 포함, 페이징)
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> list(Pageable pageable) {
        return ResponseEntity.ok(reviewService.getAdminReviewList(pageable));
    }

    // 공개/비공개 토글
    @PatchMapping("/{id}/visibility")
    public ResponseEntity<String> toggleVisibility(@PathVariable Long id) {
        reviewService.toggleReviewVisibility(id);
        return ResponseEntity.ok("공개 상태 변경 완료");
    }

    // 강제 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        reviewService.adminDeleteReview(id);
        return ResponseEntity.ok("삭제 완료");
    }
}
