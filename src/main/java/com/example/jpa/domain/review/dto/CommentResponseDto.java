package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private Long id;
    private Long userId;
    private String authorAccountId; // [보완] 댓글 작성자 로그인 아이디
    private String content;
    private Long parentId;
    private Boolean isSecret;
    private Boolean isDeleted; // 삭제된 댓글인지 여부
    private LocalDateTime createdAt;

    @Builder.Default
    private List<CommentResponseDto> children = new ArrayList<>(); //대댓글 트리 구조



}
