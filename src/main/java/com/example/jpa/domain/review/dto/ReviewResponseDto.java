package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {

    private Long id;
    private String title;
    private String content;
    private Long userId; // 작성자 PK
    private String authorAccountId; // [보완] 작성자 로그인 아이디
    private Long planId;
    private Integer rating;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ReviewImageResponseDto> images; // 이미지 상세 정보 목록

    private String thumbnailUrl; // 썸네일 이미지 경로
    // ReviewResponseDto.java에 추가
    private List<CommentResponseDto> comments;

}
