package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {

    private Long userId;      // 댓글 작성자 ID
    private String content;   // 댓글 내용
    private Long parentId;    // 부모 댓글 ID (대댓글일 경우에만 사용, 일반 댓글은 null)
    private Boolean isSecret; // 비밀 댓글 여부
}