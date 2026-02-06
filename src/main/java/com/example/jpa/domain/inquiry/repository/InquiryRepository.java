package com.example.jpa.domain.inquiry.repository;

import com.example.jpa.domain.inquiry.entity.Inquiry;
import com.example.jpa.domain.inquiry.entity.InquiryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    // 삭제되지 않은 문의 목록 (사용자용 - 본인 것만)
    Page<Inquiry> findByWriterIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    // 삭제되지 않은 전체 문의 목록 (관리자용)
    Page<Inquiry> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

    // 상태별 조회 (관리자용)
    Page<Inquiry> findByStatusAndIsDeletedFalseOrderByCreatedAtDesc(InquiryStatus status, Pageable pageable);

    // 카테고리별 조회
    Page<Inquiry> findByCategoryAndIsDeletedFalseOrderByCreatedAtDesc(String category, Pageable pageable);

    // 상세 조회 (삭제되지 않은 것만)
    Optional<Inquiry> findByIdAndIsDeletedFalse(Long id);

    // 사용자의 특정 문의 조회 (권한 체크용)
    Optional<Inquiry> findByIdAndWriterIdAndIsDeletedFalse(Long id, Long userId);

    // 답변 대기 중인 문의 수 (관리자 대시보드용)
    long countByStatusAndIsDeletedFalse(InquiryStatus status);

    // 검색 (제목 + 내용)
    @Query("SELECT i FROM Inquiry i WHERE i.isDeleted = false AND " +
           "(i.title LIKE %:keyword% OR i.content LIKE %:keyword%) " +
           "ORDER BY i.createdAt DESC")
    Page<Inquiry> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
