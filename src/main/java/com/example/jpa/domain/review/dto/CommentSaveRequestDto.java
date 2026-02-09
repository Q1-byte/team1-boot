package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentSaveRequestDto {

    private Long reviewId;
    private Long userId;
    private Long parentId; // 일반 댓글은 null, 대댓글은 부모 댓글의 PK
    private String content;
    private Boolean isSecret;
}
