package com.example.jpa.controller;

import com.example.jpa.domain.payment.repository.PaymentRepository;
import com.example.jpa.domain.review.repository.ReviewRepository;
import com.example.jpa.domain.spot.repository.SpotRepository;
import com.example.jpa.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class MainApiController {

    private final MemberRepository memberRepository;
    private final SpotRepository spotRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalMembers", memberRepository.count());
        stats.put("totalSpots", spotRepository.count());
        stats.put("totalPayments", paymentRepository.count());
        stats.put("totalReviews", reviewRepository.count());

        return ResponseEntity.ok(stats);
    }
}
