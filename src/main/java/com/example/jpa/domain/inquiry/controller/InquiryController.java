package com.example.jpa.domain.inquiry.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.inquiry.dto.InquiryCategoryDto;
import com.example.jpa.domain.inquiry.dto.InquiryCreateRequest;
import com.example.jpa.domain.inquiry.dto.InquiryDto;
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

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
@Log4j2
public class InquiryController {

    private final InquiryService inquiryService;

    // 문의 카테고리 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<InquiryCategoryDto>>> getCategories() {
        List<InquiryCategoryDto> categories = inquiryService.getCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    // 내 문의 목록 조회
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<InquiryDto>>> getMyInquiries(
            @PageableDefault(size = 10) Pageable pageable,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("로그인이 필요합니다."));
        }

        Page<InquiryDto> inquiries = inquiryService.getMyInquiries(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(inquiries));
    }

    // 문의 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InquiryDto>> getInquiry(
            @PathVariable Long id,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        boolean isAdmin = "ADMIN".equals(role);

        try {
            InquiryDto inquiry = inquiryService.getInquiry(id, userId, isAdmin);
            return ResponseEntity.ok(ApiResponse.success(inquiry));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(ApiResponse.notFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(ApiResponse.error(e.getMessage()));
        }
    }

    // 문의 등록
    @PostMapping
    public ResponseEntity<ApiResponse<InquiryDto>> createInquiry(
            @Valid @RequestBody InquiryCreateRequest request,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("로그인이 필요합니다."));
        }

        InquiryDto created = inquiryService.createInquiry(userId, request);
        return ResponseEntity.ok(ApiResponse.success(created));
    }

    // 문의 삭제 (본인만, 답변 전만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, String>>> deleteInquiry(
            @PathVariable Long id,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("로그인이 필요합니다."));
        }

        try {
            inquiryService.deleteInquiry(id, userId);
            return ResponseEntity.ok(ApiResponse.success(Map.of("message", "문의가 삭제되었습니다.")));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(ApiResponse.notFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}
