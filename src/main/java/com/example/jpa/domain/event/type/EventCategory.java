package com.example.jpa.domain.event.type;

import lombok.Getter;

@Getter
public enum EventCategory {
    FESTIVAL("축제", "A0208"),
    FOOD("먹거리", "A0502"),
    SEASON("시즌테마", "A0207"),
    GENERAL("일반행사", "A0000");

    private final String description; // 한글 명칭
    private final String publicCode; // 공공데이터 포털 cat2 매칭 코드

    EventCategory(String description, String publicCode) {
        this.description = description;
        this.publicCode = publicCode;
    }
}