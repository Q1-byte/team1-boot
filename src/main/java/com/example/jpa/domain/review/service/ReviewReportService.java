package com.example.jpa.domain.review.service;

import com.example.jpa.domain.review.dto.ReportRequestDto;
import com.example.jpa.domain.review.entity.Review;
import com.example.jpa.domain.review.entity.ReviewReport;
import com.example.jpa.domain.review.repository.ReviewReportRepository;
import com.example.jpa.domain.review.repository.ReviewRepository;
import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewReportService {

    private final ReviewReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    /**
     * 신고 접수 로직
     */
    public Long createReport(ReportRequestDto dto) {
        // 1. 신고 대상 리뷰 존재 확인
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new IllegalArgumentException("신고할 리뷰가 존재하지 않습니다. ID: " + dto.getReviewId()));

        // 2. 신고자(User) 존재 확인
        User reporter = userRepository.findById(dto.getReporterId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. ID: " + dto.getReporterId()));

        // 3. 엔티티 생성 (category와 reason을 각각 매핑)
        ReviewReport report = ReviewReport.builder()
                .review(review)
                .reporter(reporter)
                .category(dto.getCategory()) // 🚩 DTO에 추가한 category 사용
                .reason(dto.getReason())
                .build();

        // 4. 저장 및 ID 반환
        return reportRepository.save(report).getId();
    }
}