package com.example.jpa.domain.payment.service;

import com.example.jpa.domain.payment.dto.KakaoPayReadyResponse;
import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
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
    private final String KAKAO_ADMIN_KEY = "a93cf78c5c79c5fbefab2c183750252b";

    /**
     * 1단계: 결제 준비 (Ready)
     */
    public KakaoPayReadyResponse kakaoPayReady(Map<String, Object> requestData) {
        RestTemplate restTemplate = new RestTemplate();

        // 파라미터 추출 및 기본값 설정
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

        // [중요 수정] 프론트엔드 라우터 경로와 일치시켜야 합니다 (포트 5173 기준)
        params.add("approval_url", "http://localhost:5173/payment/kakao/success");
        params.add("cancel_url", "http://localhost:5173/payment/cancel");
        params.add("fail_url", "http://localhost:5173/payment/kakao/fail");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        KakaoPayReadyResponse response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoPayReadyResponse.class);

        // DB 저장 (READY)
        if (response != null) {
            Payment payment = Payment.builder()
                    .tid(response.getTid())
                    .partnerOrderId(partnerOrderId)
                    .partnerUserId(partnerUserId)
                    .itemName(itemName)
                    .totalAmount(Integer.parseInt(totalAmount))
                    .status("READY")
                    .createdAt(LocalDateTime.now())
                    .build();

            paymentRepository.save(payment);
        }

        return response;
    }

    /**
     * 2단계: 결제 승인 (Approve)
     */
    @Transactional
    public String kakaoPayApprove(String tid, String pgToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        Payment paymentInfo = paymentRepository.findByTid(tid)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", tid);
        params.add("partner_order_id", paymentInfo.getPartnerOrderId());
        params.add("partner_user_id", paymentInfo.getPartnerUserId());
        params.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        //
        String response = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                String.class);

        // 상태 업데이트: Builder 방식보다는 Entity 내부의 update 메서드나 Dirty Checking 추천
        paymentInfo.updateStatus("COMPLETED", tid); // Payment 엔티티에 메서드 생성 권장
        paymentRepository.save(paymentInfo);

        return response;
    }
}