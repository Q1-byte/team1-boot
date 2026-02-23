package com.example.jpa.domain.payment.service;

import com.example.jpa.domain.payment.dto.TossPaymentConfirmRequest;
import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.repository.PaymentRepository;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import com.example.jpa.domain.point.entity.Point;
import com.example.jpa.domain.point.repository.PointRepository;
import com.example.jpa.domain.user.repository.UserRepository;
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
    private final TravelPlanRepository travelPlanRepository;
    private final UserRepository userRepository;
    private final PointRepository pointRepository;
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
                    .orElseGet(() -> Payment.builder()
                            .partnerOrderId(request.getOrderId())
                            .planId(request.getPlanId())
                            .totalAmount(Math.toIntExact(request.getAmount()))
                            .createdAt(LocalDateTime.now())
                            .build());

            if ("가상계좌".equals(method)) {
                payment.updateStatus("WAITING_FOR_DEPOSIT", request.getPaymentKey());
                body.put("earnedPoint", 0);
            } else {
                payment.updateStatus("COMPLETED", request.getPaymentKey());

                // TravelPlan 상태 PAID + totalPrice 업데이트
                if (payment.getPlanId() != null) {
                    travelPlanRepository.findById(payment.getPlanId())
                            .ifPresent(plan -> {
                                plan.setStatus("PAID");
                                plan.setTotalPrice(payment.getTotalAmount());
                            });
                }

                // 포인트 차감 및 적립
                Long userId = payment.getUserId() != null ? payment.getUserId() : request.getUserId();
                int usePoint = request.getUsePoint() != null ? request.getUsePoint() : 0;
                int earnedPoint = (int) (payment.getTotalAmount() * 0.005);
                if (userId != null) {
                    userRepository.findById(userId).ifPresent(user -> {
                        // 포인트 차감
                        if (usePoint > 0) {
                            int actualUsePoint = Math.min(usePoint, user.getPoint());
                            user.setPoint(user.getPoint() - actualUsePoint);
                            pointRepository.save(Point.builder()
                                    .userId(user.getId())
                                    .amount(-actualUsePoint)
                                    .description("결제 포인트 사용")
                                    .build());
                        }
                        // 포인트 적립 (실결제금액의 1%)
                        if (earnedPoint > 0) {
                            user.setPoint(user.getPoint() + earnedPoint);
                            pointRepository.save(Point.builder()
                                    .userId(user.getId())
                                    .amount(earnedPoint)
                                    .description("결제 포인트 적립 (결제금액: " + payment.getTotalAmount() + "원)")
                                    .build());
                        }
                    });
                }
                body.put("earnedPoint", earnedPoint);
            }

            paymentRepository.save(payment);
            return body;
        }

        throw new RuntimeException("결제 승인에 실패했습니다.");
    }
}