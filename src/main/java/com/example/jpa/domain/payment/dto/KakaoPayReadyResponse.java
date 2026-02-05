package com.example.jpa.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayReadyResponse {
    private String tid;                  // 결제 고유 번호
    private String next_redirect_pc_url; // 카카오톡 결제 페이지 URL (PC용)
    private String created_at;           // 결제 준비 요청 시간
}
