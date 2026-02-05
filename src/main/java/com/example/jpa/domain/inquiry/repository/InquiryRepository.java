package com.example.jpa.domain.inquiry.repository;

import com.example.jpa.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
    // 필요한 경우 특정 회원의 문의글만 찾거나, 상태별 조회를 추가할 수 있습니다.
}
