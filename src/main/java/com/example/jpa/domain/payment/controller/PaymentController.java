package com.example.jpa.domain.payment.controller;

import com.example.jpa.domain.payment.dto.KakaoPayReadyResponse;
import com.example.jpa.domain.payment.dto.TossPaymentConfirmRequest;
import com.example.jpa.domain.payment.service.KakaoPayService;
import com.example.jpa.domain.payment.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final KakaoPayService kakaoPayService;
    private final TossPaymentService tossPaymentService;

    /**
     * 1. 결제 준비 (Ready)
     * 리액트에서 '결제하기' 버튼 클릭 시 호출
     */
    @PostMapping("/ready")
    public KakaoPayReadyResponse ready(@RequestBody Map<String, Object> requestData) {
        return kakaoPayService.kakaoPayReady(requestData);
    }

    /**
     * 2. 결제 승인 (Approve)
     * 리액트의 PaymentSuccess.jsx에서 pg_token을 받은 뒤 호출
     */
    @PostMapping("/approve")
    public Map<String, Object> approve(@RequestBody Map<String, String> requestData) {
        String tid = requestData.get("tid");
        String pgToken = requestData.get("pg_token");
        int usePoint = Integer.parseInt(requestData.getOrDefault("use_point", "0"));
        return kakaoPayService.kakaoPayApprove(tid, pgToken, usePoint);
    }

    /**
     * 3. 토스 결제 승인 (Confirm)
     */
    @PostMapping("/toss/confirm")
    public Map<String, Object> tossConfirm(@RequestBody TossPaymentConfirmRequest request) {
        return tossPaymentService.confirmPayment(request);
    }
}