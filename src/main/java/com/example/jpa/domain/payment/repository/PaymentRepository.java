package com.example.jpa.domain.payment.repository;

import com.example.jpa.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPartnerOrderId(String partnerOrderId);

    Optional<Payment> findByTid(String tid);
}
