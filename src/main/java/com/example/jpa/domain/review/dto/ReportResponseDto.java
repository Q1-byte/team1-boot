package com.example.jpa.domain.review.dto;

import com.example.jpa.domain.review.type.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponseDto {

    private Long id;
    private Long reviewId;
    private String reporterAccountId; // [추가] 신고자 로그인 아이디
    private String reason;
    private ReportStatus status;
    private LocalDateTime createdAt;
}
