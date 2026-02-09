package com.example.jpa.domain.review.controller;

import com.example.jpa.domain.review.dto.ReportRequestDto;
import com.example.jpa.domain.review.service.ReviewReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 리액트(Vite) 포트 허용 (Config 파일이 없을 경우 대비)
public class ReviewReportController {

    private final ReviewReportService reviewReportService;

    /**
     * 리액트의 신고 모달에서 제출 버튼을 누르면 호출되는 API
     */
    @PostMapping
    public ResponseEntity<Long> createReport(@RequestBody ReportRequestDto dto) {
        // 서비스 호출하여 DB 저장
        Long reportId = reviewReportService.createReport(dto);

        // 성공 시 생성된 신고 ID와 함께 200 OK 응답
        return ResponseEntity.ok(reportId);
    }
}