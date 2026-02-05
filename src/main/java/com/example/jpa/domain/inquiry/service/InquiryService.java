package com.example.jpa.domain.inquiry.service;

import com.example.jpa.domain.inquiry.dto.InquiryDto;
import com.example.jpa.domain.inquiry.entity.Inquiry;
import com.example.jpa.domain.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정하여 성능 최적화
public class InquiryService {

    private final InquiryRepository inquiryRepository;

    // 문의 등록 (사용자용)
    @Transactional
    public void insert(Inquiry inquiry) {
        inquiryRepository.save(inquiry);
    }

    // 문의 삭제 (관리자 또는 본인용)
    @Transactional
    public void delete(int id) {
        inquiryRepository.deleteById(id);
    }

    // 단건 조회
    public InquiryDto findById(int id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("내역 없음"));

// 2. 만약 DTO로 반환해야 한다면 변환 로직 추가
        InquiryDto response = new InquiryDto();
        response.setTitle(inquiry.getTitle()); // 이런 식으로 하나씩 옮겨 담기

        // Entity -> DTO 변환 (빌더 패턴 사용)
        return InquiryDto.builder()
                .id(inquiry.getWriter().getMemberId())
                .title(inquiry.getTitle())
                .writerName(inquiry.getWriter().getName()) // 연관관계 활용
                .status(inquiry.getStatus())
                .regDate(inquiry.getRegDate())
                .build();

    }

    // 전체 리스트 조회 (관리자용)
    public List<Inquiry> findByAll() {
        return inquiryRepository.findAll();
    }

    // 페이징 처리된 리스트 조회 (관리자 페이지용)
    public Page<Inquiry> findByAll(Pageable pageable) {
        log.info("Inquiry List Paging: " + pageable);
        return inquiryRepository.findAll(pageable);
    }

    /**
     * 핵심 로직: 관리자 답변 등록 및 상태 변경
     */
    @Transactional
    public void answerInquiry(int id, String comment) {
        log.info("Answering Inquiry ID: " + id);

        // 1. 해당 문의글을 찾는다
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("답변할 문의글이 없습니다."));

        // 2. 답변을 등록하고 상태를 바꾼다 (엔티티 내부 메서드 활용)
        inquiry.updateAnswer(comment);

        // @Transactional이 붙어있으므로 save()를 명시적으로 부르지 않아도
        // 메서드가 끝날 때 DB에 변경사항이 자동 저장됩니다. (더티 체킹)
    }
}