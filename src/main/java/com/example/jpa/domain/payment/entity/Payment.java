package com.example.jpa.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오페이용 결제 키
    private String tid;

    // 토스페이먼츠용 결제 키 (추가)
    private String paymentKey;

    @Column(nullable = false, unique = true)
    private String partnerOrderId; // 가맹점 주문번호 (orderId)

    @Column(nullable = false)
    private String partnerUserId; // 가맹점 회원 id

    private String itemName; // 상품명
    private Integer totalAmount; // 결제 금액

    private String status; // 상태 (READY, COMPLETED, CANCELLED)

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    /**
     * 결제 상태 및 키 업데이트 메서드
     */
    public void updateStatus(String status, String paymentKey) {
        this.status = status;
        this.paymentKey = paymentKey;
        this.approvedAt = LocalDateTime.now();
    }
}
