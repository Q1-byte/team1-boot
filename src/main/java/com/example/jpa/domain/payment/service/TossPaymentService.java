package com.example.jpa.domain.payment.service;

import com.example.jpa.domain.payment.dto.TossPaymentConfirmRequest;
import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TossPaymentService {

    private final PaymentRepository paymentRepository;
    private final String TOSS_SECRET_KEY = "test_sk_GjLJoQ1aVZp9PZn7m5YJ8w6KYe2R";

    @Transactional
    public Map<String, Object> confirmPayment(TossPaymentConfirmRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 토스 인증 헤더 설정
        String encodedKey = Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TossPaymentConfirmRequest> entity = new HttpEntity<>(request, headers);

        // 2. 토스 승인 API 호출 (가상계좌 발급 요청)
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/confirm",
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            String method = (String) body.get("method"); // "가상계좌" 인지 확인

            Payment payment = paymentRepository.findByPartnerOrderId(request.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

            if ("가상계좌".equals(method)) {
                // 가상계좌는 발급만 된 상태이므로 "WAITING_FOR_DEPOSIT" 상태로 저장
                payment.updateStatus("WAITING_FOR_DEPOSIT", request.getPaymentKey());
            } else {
                // 카드 결제 등은 즉시 완료
                payment.updateStatus("COMPLETED", request.getPaymentKey());
            }

            paymentRepository.save(payment);
            return body;
        }

        throw new RuntimeException("결제 승인에 실패했습니다.");
    }
}