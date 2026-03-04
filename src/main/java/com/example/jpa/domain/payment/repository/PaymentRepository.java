package com.example.jpa.domain.payment.repository;

import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPartnerOrderId(String partnerOrderId);

    Optional<Payment> findByTid(String tid);

    // 어드민 목록 조회 (READY 제외, status 필터 optional)
    @Query("SELECT p FROM Payment p WHERE p.status <> com.example.jpa.domain.payment.entity.PaymentStatus.READY AND (:status IS NULL OR p.status = :status)")
    Page<Payment> searchPayments(@Param("status") PaymentStatus status, Pageable pageable);

    // COMPLETED 상태 결제 누적 합계
    @Query("SELECT COALESCE(SUM(p.totalAmount), 0) FROM Payment p WHERE p.status = com.example.jpa.domain.payment.entity.PaymentStatus.COMPLETED")
    Long sumCompletedAmount();
}
