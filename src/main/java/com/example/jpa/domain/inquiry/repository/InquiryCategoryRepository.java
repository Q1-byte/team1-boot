package com.example.jpa.domain.inquiry.repository;

import com.example.jpa.domain.inquiry.entity.InquiryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryCategoryRepository extends JpaRepository<InquiryCategory, String> {

    // 활성화된 카테고리만 정렬하여 조회
    List<InquiryCategory> findByIsActiveTrueOrderByDisplayOrderAsc();
}
