package com.example.jpa.domain.inquiry.service;

import com.example.jpa.domain.inquiry.dto.*;
import com.example.jpa.domain.inquiry.entity.Inquiry;
import com.example.jpa.domain.inquiry.entity.InquiryCategory;
import com.example.jpa.domain.inquiry.entity.InquiryStatus;
import com.example.jpa.domain.inquiry.repository.InquiryCategoryRepository;
import com.example.jpa.domain.inquiry.repository.InquiryRepository;
import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    // ==================== 사용자용 ====================

    // 문의 등록
    @Transactional
    public InquiryDto createInquiry(Long userId, InquiryCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다."));

        Inquiry inquiry = Inquiry.builder()
                .writer(user)
                .category(request.getCategory())
                .title(request.getTitle())
                .content(request.getContent())
                .isSecret(request.getIsSecret())
                .build();

        Inquiry saved = inquiryRepository.save(inquiry);
        log.info("문의 등록 완료 - ID: {}, 작성자: {}", saved.getId(), user.getUsername());

        return InquiryDto.fromEntity(saved);
    }

    // 내 문의 목록 조회
    public Page<InquiryDto> getMyInquiries(Long userId, Pageable pageable) {
        return inquiryRepository.findByWriterIdAndIsDeletedFalseOrderByCreatedAtDesc(userId, pageable)
                .map(InquiryDto::fromEntityForList);
    }

    // 문의 상세 조회 (권한 체크 포함)
    public InquiryDto getInquiry(Long inquiryId, Long userId, boolean isAdmin) {
        Inquiry inquiry = inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new NoSuchElementException("문의를 찾을 수 없습니다."));

        // 비밀글인 경우 본인 또는 관리자만 조회 가능
        if (Boolean.TRUE.equals(inquiry.getIsSecret()) && !isAdmin && !inquiry.getWriter().getId().equals(userId)) {
            throw new IllegalStateException("접근 권한이 없습니다.");
        }

        return InquiryDto.fromEntity(inquiry);
    }

    // 문의 삭제 (사용자용 - 답변 전만 가능)
    @Transactional
    public void deleteInquiry(Long inquiryId, Long userId) {
        Inquiry inquiry = inquiryRepository.findByIdAndWriterIdAndIsDeletedFalse(inquiryId, userId)
                .orElseThrow(() -> new NoSuchElementException("문의를 찾을 수 없습니다."));

        if (inquiry.getStatus() == InquiryStatus.ANSWERED) {
            throw new IllegalStateException("답변이 완료된 문의는 삭제할 수 없습니다.");
        }

        inquiry.softDelete();
        log.info("문의 삭제 완료 - ID: {}", inquiryId);
    }

    // 카테고리 목록 조회
    public List<InquiryCategoryDto> getCategories() {
        return categoryRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(InquiryCategoryDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ==================== 관리자용 ====================

    // 전체 문의 목록 조회
    public Page<InquiryDto> getAllInquiries(Pageable pageable) {
        return inquiryRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable)
                .map(InquiryDto::fromEntityForList);
    }

    // 상태별 문의 목록 조회
    public Page<InquiryDto> getInquiriesByStatus(InquiryStatus status, Pageable pageable) {
        return inquiryRepository.findByStatusAndIsDeletedFalseOrderByCreatedAtDesc(status, pageable)
                .map(InquiryDto::fromEntityForList);
    }

    // 문의 상세 조회 (관리자용 - 권한 체크 없음)
    public InquiryDto getInquiryForAdmin(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new NoSuchElementException("문의를 찾을 수 없습니다."));
        return InquiryDto.fromEntity(inquiry);
    }

    // 답변 등록
    @Transactional
    public void answerInquiry(Long inquiryId, Long adminId, InquiryAnswerRequest request) {
        Inquiry inquiry = inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new NoSuchElementException("문의를 찾을 수 없습니다."));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new NoSuchElementException("관리자를 찾을 수 없습니다."));

        inquiry.updateAnswer(request.getAnswer(), admin);
        log.info("문의 답변 완료 - ID: {}, 답변자: {}", inquiryId, admin.getUsername());
    }

    // 문의 삭제 (관리자용)
    @Transactional
    public void deleteInquiryByAdmin(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findByIdAndIsDeletedFalse(inquiryId)
                .orElseThrow(() -> new NoSuchElementException("문의를 찾을 수 없습니다."));
        inquiry.softDelete();
        log.info("관리자 문의 삭제 - ID: {}", inquiryId);
    }

    // 답변 대기 문의 수
    public long getWaitingCount() {
        return inquiryRepository.countByStatusAndIsDeletedFalse(InquiryStatus.WAIT);
    }

    // 검색
    public Page<InquiryDto> searchInquiries(String keyword, Pageable pageable) {
        return inquiryRepository.searchByKeyword(keyword, pageable)
                .map(InquiryDto::fromEntityForList);
    }
}
