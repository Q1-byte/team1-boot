package com.example.jpa.domain.payment.controller;

import com.example.jpa.domain.payment.service.PaymentService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PointPaymentController {

    private final PaymentService paymentService;

    /**
     * 포인트 전액 결제 (외부 PG 미사용)
     * POST /payment/point-only
     * Body: { "plan_id": 10, "user_id": 1, "use_point": 30000 }
     */
    @PostMapping("/point-only")
    public ResponseEntity<?> pointOnlyPayment(@RequestBody PointOnlyRequest request) {
        try {
            paymentService.pointOnlyPayment(request.getUserId(), request.getPlanId(), request.getUsePoint());
            return ResponseEntity.ok().body(java.util.Map.of("result", "success", "usedPoints", request.getUsePoint(), "earnedPoint", 0));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @Getter
    @Setter
    public static class PointOnlyRequest {
        @JsonProperty("user_id")
        private Long userId;

        @JsonProperty("plan_id")
        private Long planId;

        @JsonProperty("use_point")
        private Integer usePoint;
    }
}
