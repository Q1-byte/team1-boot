package com.example.jpa.domain.review.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewImageRequestDto {
    private String originName;  // 원본 파일명 (예: photo.jpg)
    private String storedUrl;   // S3나 서버에 저장된 실제 경로
    private Integer sortOrder;  // 이미지 출력 순서
}