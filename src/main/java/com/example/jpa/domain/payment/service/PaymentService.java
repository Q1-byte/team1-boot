package com.example.jpa.domain.payment.service;

import com.example.jpa.domain.history.service.HistoryService;
import com.example.jpa.domain.payment.entity.Payment;
import com.example.jpa.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final HistoryService historyService;

    // 전체 결제 내역 조회 (페이징)
    public Page<Payment> getPaymentList(Pageable pageable) {
        return paymentRepository.findAll(pageable);
    }

    // 오늘 총 매출액 계산 (Repository에 관련 메서드가 있다고 가정)
    public Long getTotalSalesToday() {
        // Repository에서 직접 sum 쿼리를 날리거나 로직 처리
        // return paymentRepository.sumAmountByDate(LocalDate.now());
        return 0L; // 우선 0으로 반환 (필요시 쿼리 추가)
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