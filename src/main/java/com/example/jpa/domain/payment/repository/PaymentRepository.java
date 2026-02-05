package com.example.jpa.domain.payment.repository;

import com.example.jpa.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // 이 줄을 추가하면 빨간 줄이 사라집니다!
    Optional<Payment> findByPartnerOrderId(String partnerOrderId);

    // 카카오페이 승인 때 사용했던 메서드
    Optional<Payment> findByTid(String tid);
}