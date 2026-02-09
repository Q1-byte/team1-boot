package com.example.jpa.domain.review.type;

import lombok.Getter;

@Getter
public enum ReportStatus {
    PENDING("대기 중"),
    PROCESSED("처리 완료"),
    REJECTED("반려");

    private final String description;

    ReportStatus(String description) {
        this.description = description;
    }
}
