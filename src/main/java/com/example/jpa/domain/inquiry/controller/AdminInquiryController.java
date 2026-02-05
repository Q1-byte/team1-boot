package com.example.jpa.domain.inquiry.controller;

import com.example.jpa.domain.inquiry.dto.InquiryDto;
import com.example.jpa.domain.inquiry.entity.Inquiry;
import com.example.jpa.domain.inquiry.service.InquiryService;
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
@CrossOrigin(origins = "http://localhost:3000")
public class AdminInquiryController {

    private final InquiryService inquiryService;

    // 1. 전체 문의 목록 조회
    @GetMapping
    public ResponseEntity<Page<Inquiry>> getList(@PageableDefault(size = 10) Pageable pageable) {
        log.info("관리자 문의 목록 조회 - 페이지 설정: {}", pageable);
        return ResponseEntity.ok(inquiryService.findByAll(pageable));
    }

    // 2. 문의 등록 (추가됨)
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Inquiry inquiry) {
        // 1. 서비스의 메서드명이 'insert'이므로 이름을 맞춰줍니다.
        inquiryService.insert(inquiry);

        // 2. 서비스가 void를 반환하므로 ok() 안에 넣지 않고 build()로 응답합니다.
        return ResponseEntity.ok().build();
    }

    // 3. 문의 상세 조회 (int id -> 서비스 인터페이스에 따라 Integer 등으로 조절 필요할 수 있음)
    @GetMapping("/{id}")
    public ResponseEntity<InquiryDto> getOne(@PathVariable int id) {
        return ResponseEntity.ok(inquiryService.findById(id));
    }

    // 4. 문의 답변 등록 및 수정
    @PatchMapping("/{id}/answer")
    public ResponseEntity<Map<String, String>> answer(@PathVariable int id, @RequestBody Map<String, String> request) {
        String comment = request.get("comment");
        log.info("문의 답변 등록 - ID: {}, 내용: {}", id, comment);
        inquiryService.answerInquiry(id, comment);
        return ResponseEntity.ok(Map.of("message", "답변이 성공적으로 등록되었습니다."));
    }

    // 5. 문의글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable int id) {
        inquiryService.delete(id);
        return ResponseEntity.ok(Map.of("message", "삭제 성공"));
    }
} // <--- 클래스 닫는 중괄호는 맨 마지막에 한 번만!