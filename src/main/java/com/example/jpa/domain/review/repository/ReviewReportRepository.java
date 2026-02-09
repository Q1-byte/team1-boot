package com.example.jpa.domain.review.repository;

import com.example.jpa.domain.review.entity.ReviewReport;
import com.example.jpa.domain.review.type.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {

    List<ReviewReport> findAllByStatus(ReportStatus status);

    // 2. [추가 추천] 특정 카테고리(스팸, 욕설 등)별 신고 목록 조회
    // 리액트에서 보낸 'category' 필드로 필터링하기 위해 필요합니다.
    List<ReviewReport> findAllByCategory(String category);
}
