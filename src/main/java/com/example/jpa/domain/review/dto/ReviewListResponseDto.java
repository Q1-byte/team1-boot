package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListResponseDto {

    private Long id;
    private String title;
    private String authorAccountId; // [보완] 작성자 로그인 아이디 (예: user123)
    private Integer rating;
    private Integer viewCount;
    private Integer commentCount; // [추가] 목록에서 보여줄 댓글 수
    private String thumbnailUrl; // 대표 이미지 경로
    private LocalDateTime createdAt;
}
