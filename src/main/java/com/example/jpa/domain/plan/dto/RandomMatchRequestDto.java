package com.example.jpa.domain.plan.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RandomMatchRequestDto {

    private Long regionId;
    private String keyword;       // 선호 키워드 (예: "힐링", "액티비티")
    private Integer budgetMin;    // 최소 예산
    private Integer budgetMax;    // 최대 예산
    private LocalDate travelDate; // 여행 시작일
    private Integer durationDays; // 여행 일수 (1 = 당일치기, 2 = 1박2일 ...)
    private Integer peopleCount;  // 인원수
}
