package com.example.jpa.domain.payment.service;

import com.example.jpa.domain.payment.dto.KakaoPayReadyResponse;
import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.repository.PaymentRepository;
import com.example.jpa.domain.plan.entity.TravelPlan;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final PaymentRepository paymentRepository;
    private final TravelPlanRepository travelPlanRepository;

    @Value("${kakao.admin-key}")
    private String KAKAO_ADMIN_KEY;

    /**
     * 결제 준비 (Ready)
     */
    public KakaoPayReadyResponse kakaoPayReady(Map<String, Object> requestData) {
        RestTemplate restTemplate = new RestTemplate();

        String partnerOrderId = (String) requestData.getOrDefault("partner_order_id", "ORDER-" + UUID.randomUUID().toString().substring(0, 8));
        String partnerUserId = (String) requestData.getOrDefault("partner_user_id", "user_1234");
        String itemName = (String) requestData.getOrDefault("item_name", "여행 일정 예약");
        String totalAmount = String.valueOf(requestData.getOrDefault("total_amount", "0"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", partnerOrderId);
        params.add("partner_user_id", partnerUserId);
        params.add("item_name", itemName);
        params.add("quantity", "1");
        params.add("total_amount", totalAmount);
        params.add("tax_free_amount", "0");

        // 프런트엔드 리다이렉트 경로 (tid를 쿼리 파라미터로 붙여주면 승인 시 찾기 편합니다)
        params.add("approval_url", "http://localhost:5173/payment/kakao/success");
        params.add("cancel_url", "http://localhost:5173/payment/cancel");
        params.add("fail_url", "http://localhost:5173/payment/kakao/fail");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        KakaoPayReadyResponse response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoPayReadyResponse.class);

        if (response != null) {
            // 요청 데이터에서 userId, planId 추출 (프론트엔드에서 전달)
            Long userId = extractLongValue(requestData.get("user_id"));
            Long planId = extractLongValue(requestData.get("plan_id"));

            if (userId == null) {
                throw new IllegalArgumentException("user_id는 필수 값입니다.");
            }
            if (planId == null) {
                throw new IllegalArgumentException("plan_id는 필수 값입니다.");
            }

            Payment payment = Payment.builder()
                    .tid(response.getTid())
                    .partnerOrderId(partnerOrderId)
                    .partnerUserId(partnerUserId)
                    .userId(userId)
                    .planId(planId)
                    .totalAmount(Integer.parseInt(totalAmount))
                    .itemName(itemName)
                    .status("READY")
                    .createdAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
        }

        return response;
    }

    /**
     * 결제 승인 (Approve)
     */
    @Transactional
    public String kakaoPayApprove(String tid, String pgToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 1. DB에서 결제 대기 상태인 데이터 조회
        Payment paymentInfo = paymentRepository.findByTid(tid)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다. TID: " + tid));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", tid);
        params.add("partner_order_id", paymentInfo.getPartnerOrderId());
        params.add("partner_user_id", paymentInfo.getPartnerUserId());
        params.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        // 2. 카카오 서버로 최종 승인 요청
        // 응답을 Map이나 별도 DTO로 받아 결제 수단 등을 추출할 수 있습니다.
        Map<String, Object> response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                Map.class);

        // 3. 결제 상태 업데이트 (Dirty Checking 활용)
        if (response != null) {
            paymentInfo.updateStatus("COMPLETED", tid);

            // 4. TravelPlan 상태 PAID + totalPrice 업데이트
            if (paymentInfo.getPlanId() != null) {
                travelPlanRepository.findById(paymentInfo.getPlanId())
                        .ifPresent(plan -> {
                            plan.setStatus("PAID");
                            plan.setTotalPrice(paymentInfo.getTotalAmount());
                        });
            }
        }

        return "success";
    }

    /**
     * Object를 Long으로 변환하는 헬퍼 메서드
     */
    private Long extractLongValue(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
}