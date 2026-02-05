package com.example.jpa.domain.payment.controller;

import com.example.jpa.domain.payment.dto.KakaoPayReadyResponse;
import com.example.jpa.domain.payment.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController // @Controller + @ResponseBody 합친 것과 같습니다.
@RequiredArgsConstructor
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // 리액트(5173번 포트)와의 통신을 허용합니다.
public class PaymentController {

    private final KakaoPayService kakaoPayService;

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
    public String approve(@RequestBody Map<String, String> requestData) {
        // 리액트에서 보낸 tid와 pg_token을 추출합니다.
        String tid = requestData.get("tid");
        String pgToken = requestData.get("pg_token");

        return kakaoPayService.kakaoPayApprove(tid, pgToken);
    }
}