package com.example.jpa.domain.review.service;

import com.example.jpa.domain.review.dto.CommentRequestDto;
import com.example.jpa.domain.review.entity.Review;
import com.example.jpa.domain.review.entity.ReviewComment;
import com.example.jpa.domain.review.repository.ReviewCommentRepository;
import com.example.jpa.domain.review.repository.ReviewRepository;
import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewCommentService {

    private final ReviewCommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    /**
     * [1] 댓글/대댓글 등록
     */
    @Transactional
    public Long saveComment(Long reviewId, CommentRequestDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        ReviewComment parent = null;
        // parentId가 전달되었다면 대댓글로 설정
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
        }

        ReviewComment comment = ReviewComment.builder()
                .review(review)
                .user(user)
                .content(dto.getContent())
                .parent(parent) // 대댓글인 경우 부모 설정
                .isSecret(dto.getIsSecret())
                .isDeleted(false)
                .build();

        return commentRepository.save(comment).getId();
    }

    /**
     * [2] 댓글 삭제 (소프트 삭제)
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getUser().getId().equals(userId)) {
            throw new IllegalStateException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        // 실제 삭제 대신 상태값만 변경
        comment.setIsDeleted(true);
    }
}
