package com.example.jpa.domain.review.controller;

import com.example.jpa.domain.review.dto.ReviewResponseDto;
import com.example.jpa.domain.review.dto.ReviewSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 1. 리뷰 등록
    @PostMapping
    public ResponseEntity<Long> saveReview(@RequestBody ReviewSaveRequestDto dto) {
        return ResponseEntity.ok(reviewService.saveReview(dto));
    }

    // 2. 리뷰 목록 조회 (페이징: 한 페이지당 10개)
    @GetMapping
    public ResponseEntity<Page<ReviewResponseDto>> getReviewList(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(reviewService.getReviewList(pageable));
    }

    // 3. 리뷰 상세 조회 (댓글 포함)
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> getReviewDetail(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewDetail(reviewId));
    }

    // 4. 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<Long> updateReview(
            @PathVariable Long reviewId,
            @RequestBody ReviewSaveRequestDto dto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, dto));
    }

    // 5. 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long userId) { // 실제 서비스에선 인증 정보에서 userId를 가져옵니다.
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.noContent().build();
    }
}