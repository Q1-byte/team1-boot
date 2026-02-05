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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(nullable = false)
    private String partnerUserId; // 가맹점 회원 id

    @Column(name = "amount", nullable = false)
    private Integer totalAmount; // 결제 금액

    private String itemName; // 상품명
    private String status; // 상태 (READY, COMPLETED, CANCELLED)
    private String paymentMethod;

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;

    /**
     * 결제 상태 및 키 업데이트 메서드
     * 아까 인텔리제이에서 빨간 줄이 떴던 부분입니다!
     */
    public void updateStatus(String status, String paymentKey) {
        this.status = status;
        this.paymentKey = paymentKey;
        this.approvedAt = LocalDateTime.now(); // 승인 시간도 함께 기록하면 좋습니다.
    }
}