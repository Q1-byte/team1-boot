package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequestDto {

    private Long reviewId;
    private Long reporterId; // 신고하는 사람의 PK
    private String category;
    private String reason;
}
