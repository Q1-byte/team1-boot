package com.example.jpa.domain.payment.controller;

import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final PaymentService paymentService;

    // 결제 리스트 조회
    @GetMapping
    public ResponseEntity<Page<Payment>> list(Pageable pageable) {
        return ResponseEntity.ok(paymentService.getPaymentList(pageable));
    }

    // 오늘 총 매출액 확인 (대시보드 상단 카드용)
    @GetMapping("/today-sales")
    public ResponseEntity<Long> getTodaySales() {
        return ResponseEntity.ok(paymentService.getTotalSalesToday());
    }

    // 결제 강제 취소 (관리자 권한)
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancel(@PathVariable Long id) {
        paymentService.cancelPayment(id);
        return ResponseEntity.ok("결제가 취소되었습니다.");
    }
}