package com.example.jpa.domain.payment.service;

import com.example.jpa.domain.payment.dto.TossPaymentConfirmRequest;
import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.repository.PaymentRepository;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import com.example.jpa.domain.point.entity.Point;
import com.example.jpa.domain.point.repository.PointRepository;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;

@Slf4j
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
        // 1. 요청 데이터 로그 확인 (userId 유실 여부 파악용)
        log.info("Toss Confirm Request Data: {}", request);

        if (request.getUserId() == null || request.getPlanId() == null) {
            throw new IllegalArgumentException("결제 승인 요청에 필수 정보(userId 또는 planId)가 누락되었습니다.");
        }

        RestTemplate restTemplate = new RestTemplate();

        // 2. 토스 인증 헤더 설정
        String encodedKey = Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TossPaymentConfirmRequest> entity = new HttpEntity<>(request, headers);

        // 3. 토스 승인 API 호출
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/confirm",
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = (Map<String, Object>) response.getBody();
            String method = (String) body.get("method");

            // 4. 결제 정보 조회 또는 신규 생성 (userId 필수 할당)
            Payment payment = paymentRepository.findByPartnerOrderId(request.getOrderId())
                    .orElseGet(() -> Payment.builder()
                            .partnerOrderId(request.getOrderId())
                            .partnerUserId(String.valueOf(request.getUserId()))
                            .userId(request.getUserId()) // DB 제약 조건(NOT NULL) 해결
                            .planId(request.getPlanId())
                            .totalAmount(Math.toIntExact(request.getAmount()))
                            .createdAt(LocalDateTime.now())
                            .status("READY")
                            .build());

            // 5. 결제 상태 업데이트 (가상계좌 여부에 따른 처리)
            if ("가상계좌".equals(method)) {
                payment.updateStatus("WAITING_FOR_DEPOSIT", request.getPaymentKey(), method);
                body.put("earnedPoint", 0);
            } else {
                payment.updateStatus("COMPLETED", request.getPaymentKey(), method);

                // 플랜 상태 업데이트
                if (payment.getPlanId() != null) {
                    travelPlanRepository.findById(payment.getPlanId())
                            .ifPresent(plan -> {
                                plan.setStatus("PAID");
                                plan.setTotalPrice(payment.getTotalAmount());
                            });
                }

                // 6. 포인트 처리 (차감 및 적립)
                int usePoint = request.getUsePoint() != null ? request.getUsePoint() : 0;
                int earnedPoint = (int) (payment.getTotalAmount() * 0.005); // 0.5% 적립

                userRepository.findById(request.getUserId()).ifPresent(user -> {
                    // 포인트 사용 처리
                    if (usePoint > 0) {
                        int actualUsePoint = Math.min(usePoint, user.getPoint());
                        user.setPoint(user.getPoint() - actualUsePoint);
                        pointRepository.save(Point.builder()
                                .userId(user.getId())
                                .amount(-actualUsePoint)
                                .description("결제 포인트 사용")
                                .build());
                    }
                    // 포인트 적립 처리
                    if (earnedPoint > 0) {
                        user.setPoint(user.getPoint() + earnedPoint);
                        pointRepository.save(Point.builder()
                                .userId(user.getId())
                                .amount(earnedPoint)
                                .description("결제 포인트 적립 (결제금액: " + payment.getTotalAmount() + "원)")
                                .build());
                    }
                });
                body.put("earnedPoint", earnedPoint);
            }

            // 7. 최종 결제 정보 저장
            paymentRepository.save(payment);
            return body;
        }

        throw new RuntimeException("토스 결제 승인 API 호출에 실패했습니다.");
    }
}