package com.example.jpa.domain.inquiry.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.inquiry.dto.InquiryAnswerRequest;
import com.example.jpa.domain.inquiry.dto.InquiryDto;
import com.example.jpa.domain.inquiry.entity.InquiryStatus;
import com.example.jpa.domain.inquiry.service.InquiryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/inquiries")
@RequiredArgsConstructor
@Log4j2
public class AdminInquiryController {

    private final InquiryService inquiryService;

    // 전체 문의 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Page<InquiryDto>>> getList(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String status) {

        Page<InquiryDto> inquiries;
        if (status != null && !status.isEmpty()) {
            inquiries = inquiryService.getInquiriesByStatus(InquiryStatus.valueOf(status), pageable);
        } else {
            inquiries = inquiryService.getAllInquiries(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(inquiries));
    }

    // 문의 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InquiryDto>> getOne(@PathVariable Long id) {
        InquiryDto inquiry = inquiryService.getInquiryForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(inquiry));
    }

    // 답변 등록
    @PatchMapping("/{id}/answer")
    public ResponseEntity<ApiResponse<Map<String, String>>> answer(
            @PathVariable Long id,
            @Valid @RequestBody InquiryAnswerRequest request,
            HttpSession session) {

        Long adminId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (adminId == null || !"ADMIN".equals(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("관리자 권한이 필요합니다."));
        }

        inquiryService.answerInquiry(id, adminId, request);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "답변이 등록되었습니다.")));
    }

    // 문의 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> remove(@PathVariable Long id) {
        inquiryService.deleteInquiryByAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(Map.of("message", "삭제되었습니다.")));
    }

    // 답변 대기 문의 수
    @GetMapping("/waiting-count")
    public ResponseEntity<ApiResponse<Long>> getWaitingCount() {
        long count = inquiryService.getWaitingCount();
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    // 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<InquiryDto>>> search(
            @RequestParam String keyword,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<InquiryDto> results = inquiryService.searchInquiries(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(results));
    }
}
