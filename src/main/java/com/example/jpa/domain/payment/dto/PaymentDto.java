package com.example.jpa.domain.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Integer id;
    private String impUid;      // 결제 고유번호 (포트원 등)
    private Long amount;        // 결제 금액
    private String status;      // 결제 상태 (PAID, CANCELLED 등)
    private String buyerName;   // 구매자 이름
    private LocalDateTime payDate; // 결제 일시
}