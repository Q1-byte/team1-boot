package com.example.jpa.domain.inquiry.entity;

public enum InquiryStatus {
    WAIT("답변대기"),
    ANSWERED("답변완료");

    private final String description;

    InquiryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
