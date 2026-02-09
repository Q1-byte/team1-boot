package com.example.jpa.domain.review.controller;

import com.example.jpa.domain.review.dto.CommentRequestDto;
import com.example.jpa.domain.review.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews/{reviewId}/comments")
@RequiredArgsConstructor
public class ReviewCommentController {

    private final ReviewCommentService commentService;

    // 1. 댓글/대댓글 등록
    @PostMapping
    public ResponseEntity<Long> saveComment(
            @PathVariable Long reviewId,
            @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.saveComment(reviewId, dto));
    }

    // 2. 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long reviewId, // 경로 일관성을 위해 유지
            @PathVariable Long commentId,
            @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}