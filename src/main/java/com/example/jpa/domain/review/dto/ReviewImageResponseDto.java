package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImageResponseDto {

    private Long id;
    private String originName; // [추가] 원본 파일명
    private String storedUrl; // 저장된 전체 URL 경로
    private Integer sortOrder; // 출력 순서
}
