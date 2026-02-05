package com.example.jpa.domain.review.service;

import com.example.jpa.domain.history.service.HistoryService;
import com.example.jpa.domain.review.dto.ReviewDto;
import com.example.jpa.domain.review.entity.Review;
import com.example.jpa.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final HistoryService historyService;

    @Transactional
    public void addReview(ReviewDto.Request request) {
        Review review = Review.builder()
                .memberId(request.getUserId())
                .spotId(request.getSpotId())
                .content(request.getContent())
                .rating(request.getRating())
                .build();
        reviewRepository.save(review);

        historyService.log("REVIEW_POST", "리뷰 작성 (SpotID: " + request.getSpotId() + ")", review.getMemberId());
    }
    @Transactional
    public void deleteReview(Long id) {
        // 1. 해당 ID의 리뷰가 있는지 먼저 확인 (선택 사항이지만 권장)
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("삭제하려는 " + id + "번 리뷰가 존재하지 않습니다.");
        }

        // 2. 삭제 수행
        reviewRepository.deleteById(id);
    }
}