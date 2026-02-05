package com.example.jpa.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TossPaymentConfirmRequest {
    private String paymentKey; // 토스에서 발급한 결제 고유 키
    private String orderId;    // 우리가 만든 주문번호 (UUID)
    private Long amount;       // 결제 금액
}
