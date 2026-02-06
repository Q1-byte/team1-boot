package com.example.jpa.domain.inquiry.repository;

import com.example.jpa.domain.inquiry.entity.InquiryFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryFileRepository extends JpaRepository<InquiryFile, Long> {

    List<InquiryFile> findByInquiryId(Long inquiryId);

    void deleteByInquiryId(Long inquiryId);
}
