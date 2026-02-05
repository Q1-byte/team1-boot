package com.example.jpa.domain.review.controller;

import com.example.jpa.domain.review.dto.ReviewDto;
import com.example.jpa.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록 (POST)
     */
    @PostMapping
    public ResponseEntity<?> writeReview(@RequestBody Map<String, Object> payload) {
        try {
            ReviewDto.Request request = ReviewDto.Request.builder()
                    .userId(Integer.parseInt(payload.get("memberId").toString()))
                    .spotId(Integer.parseInt(payload.get("spotId").toString()))
                    .content(payload.get("content").toString())
                    .rating(Integer.parseInt(payload.get("rating").toString()))
                    .build();

            reviewService.addReview(request);
            return ResponseEntity.ok("리뷰 등록 성공!");

        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("데이터 누락: " + e.getMessage());
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("숫자 형식 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 에러: " + e.getMessage());
        }
    }

    /**
     * 리뷰 삭제 (DELETE)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable(name = "id") Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok("리뷰 " + id + "번이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("삭제 실패: " + e.getMessage());
        }
    }
}