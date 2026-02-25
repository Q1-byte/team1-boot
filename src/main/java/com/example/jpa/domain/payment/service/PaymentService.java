package com.example.jpa.domain.payment.service;

import com.example.jpa.domain.history.service.HistoryService;
import com.example.jpa.domain.payment.dto.PaymentDto;
import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.repository.PaymentRepository;
import com.example.jpa.domain.plan.entity.TravelPlan;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import com.example.jpa.domain.point.entity.Point;
import com.example.jpa.domain.point.repository.PointRepository;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final HistoryService historyService;
    private final UserRepository userRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final PointRepository pointRepository;

    // 어드민 결제 내역 조회 (페이징 + status 필터)
    public Page<PaymentDto.AdminResponse> getPaymentList(String status, Pageable pageable) {
        return paymentRepository.searchPayments(
                (status != null && !status.isBlank()) ? status : null,
                pageable
        ).map(this::toAdminResponse);
    }

    private PaymentDto.AdminResponse toAdminResponse(Payment payment) {
        return PaymentDto.AdminResponse.builder()
                .id(payment.getId())
                .username(payment.getPartnerUserId())
                .planTitle(payment.getItemName())
                .amount(payment.getTotalAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .paidAt(payment.getApprovedAt())
                .build();
    }

    // 오늘 총 매출액 계산 (Repository에 관련 메서드가 있다고 가정)
    public Long getTotalSalesToday() {
        // Repository에서 직접 sum 쿼리를 날리거나 로직 처리
        // return paymentRepository.sumAmountByDate(LocalDate.now());
        return 0L; // 우선 0으로 반환 (필요시 쿼리 추가)
    }

    /**
     * 포인트 전액 결제 (POST /api/payment/point-only)
     * 외부 PG 없이 포인트만으로 결제 처리
     */
    @Transactional
    public void pointOnlyPayment(Long userId, Long planId, Integer usePoints) {
        // 1. 유저 조회 및 포인트 잔액 확인
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다. id=" + userId));

        if (user.getPoint() == null || user.getPoint() < usePoints) {
            throw new IllegalArgumentException("포인트가 부족합니다. 보유: " + (user.getPoint() == null ? 0 : user.getPoint()) + "P, 필요: " + usePoints + "P");
        }

        // 2. 포인트 차감
        user.setPoint(user.getPoint() - usePoints);
        userRepository.save(user);

        pointRepository.save(Point.builder()
                .userId(userId)
                .amount(-usePoints)
                .description("포인트 전액 결제")
                .build());

        // 3. 플랜 상태 READY → PAID
        TravelPlan plan = travelPlanRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("해당 플랜이 존재하지 않습니다. id=" + planId));
        plan.setStatus("PAID");
        plan.setTotalPrice(0);
        travelPlanRepository.save(plan);

        // 4. 결제 이력 저장 (관리자 페이지에서 조회 가능하도록)
        paymentRepository.save(Payment.builder()
                .partnerOrderId("POINT-" + UUID.randomUUID().toString().substring(0, 8))
                .partnerUserId(String.valueOf(userId))
                .userId(userId)
                .planId(planId)
                .totalAmount(0)
                .itemName("포인트 전액 결제")
                .paymentMethod("POINT")
                .status("COMPLETED")
                .createdAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .build());
    }

    // 결제 취소 상태 변경
    @Transactional
    public void cancelPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 결제 내역이 없습니다. id=" + id));

        payment.updateStatus("CANCELLED", payment.getPaymentKey());

        // 히스토리 기록
        historyService.log("PAYMENT_CANCEL", "관리자가 결제를 취소함", id.intValue());
    }
}