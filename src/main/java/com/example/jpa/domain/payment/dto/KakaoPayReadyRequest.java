package com.example.jpa.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayReadyRequest {
    private String cid;             // 가맹점 코드 (테스트용: TC0ONETIME)
    private String partner_order_id; // 가맹점 주문번호
    private String partner_user_id;  // 가맹점 회원 id
    private String item_name;        // 상품명
    private Integer quantity;        // 상품 수량
    private Integer total_amount;    // 상품 총액
    private Integer tax_free_amount; // 상품 비과세 금액
    private String approval_url;     // 결제 성공 시 리다이렉트 URL
    private String cancel_url;       // 결제 취소 시 리다이렉트 URL
    private String fail_url;         // 결제 실패 시 리다이렉트 URL
}