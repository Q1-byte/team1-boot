package com.example.jpa.domain.review.service;

import com.example.jpa.domain.review.dto.*;
import com.example.jpa.domain.review.entity.Review;
import com.example.jpa.domain.review.entity.ReviewComment;
import com.example.jpa.domain.review.entity.ReviewImage;
import com.example.jpa.domain.review.repository.ReviewCommentRepository;
import com.example.jpa.domain.review.repository.ReviewImageRepository;
import com.example.jpa.domain.review.repository.ReviewRepository;
import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewCommentRepository reviewCommentRepository;

    /**
     * [조회 1] 리뷰 목록 조회 (썸네일 포함, 페이징 지원)
     */
    public Page<ReviewResponseDto> getReviewList(Pageable pageable) {
        // 1. 삭제되지 않은 리뷰들을 페이징하여 가져옵니다.
        Page<Review> reviews = reviewRepository.findAllByIsDeletedFalseOrderByCreatedAtDesc(pageable);

        // 2. 각 리뷰를 DTO로 변환하면서 썸네일을 찾습니다.
        return reviews.map(review -> {
            // 해당 리뷰의 이미지 중 첫 번째(sortOrder가 가장 작은) 이미지를 조회
            String thumbnail = reviewImageRepository.findAllByReviewIdOrderBySortOrderAsc(review.getId())
                    .stream()
                    .findFirst() // 첫 번째 이미지 선택
                    .map(ReviewImage::getStoredUrl) // 이미지의 URL 추출
                    .orElse(null); // 이미지가 없으면 null
            System.out.println("🚩 [로그] 리뷰 ID: " + review.getId() + " | 꺼내온 별점: " + review.getRating());
            return ReviewResponseDto.builder()
                    .id(review.getId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .userId(review.getUser().getId())
                    .authorAccountId(review.getUser().getUsername())
                    .viewCount(review.getViewCount())
                    .thumbnailUrl(thumbnail) // [핵심] 썸네일 주입!
                    .rating(review.getRating()) // 👈 [추가] 이 줄이 여기에도 꼭 있어야 합니다!
                    .createdAt(review.getCreatedAt())
                    .build();
        });
    }

    // 리뷰 등록
    @Transactional
    public Long saveReview(ReviewSaveRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. Id: " + dto.getUserId()));

        // 2. 리뷰 엔티티를 빌더로 조립합니다.

        Review review = Review.builder()
                .user(user)
                .title(dto.getTitle())
                .content(dto.getContent())
                .planId(dto.getPlanId())
                .rating(dto.getRating())
                .viewCount(0)
                .isDeleted(false)
                .build();

        // 3. 리뷰를 먼저 DB에 저장합니다.
        Review savedReview = reviewRepository.save(review);

        // 4. 리뷰 이미지가 있다면 처리합니다.
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            dto.getImages().forEach(imgDto -> {
                ReviewImage reviewImage = ReviewImage.builder()
                        .review(savedReview)
                        .originName(imgDto.getOriginName())
                        .storedUrl(imgDto.getStoredUrl())
                        .sortOrder(imgDto.getSortOrder())
                        .build();
                reviewImageRepository.save(reviewImage);
            });
        }
        return savedReview.getId();
    }

    // [2] 리뷰 상세 조회
    @Transactional
    public ReviewResponseDto getReviewDetail(Long reviewId) {

        // 1. 리뷰 엔티티 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID: " + reviewId));

        // [추가] 삭제된 리뷰인지 체크
        if (review.getIsDeleted()) {
            throw new IllegalStateException("삭제된 리뷰입니다.");
        }

        // [추가] 조회수 1 증가
        // Review 엔티티에 직접 viewCount를 올리는 메서드를 호출하거나 필드를 수정합니다.
        review.increaseViewCount();

        // 2. 작성자 정보 확인 (팀원의 User 엔티티)
        User user = review.getUser();

        // 3. 이미지 목록 조회 및 변환
        List<ReviewImageResponseDto> imageDtos = reviewImageRepository.findAllByReviewIdOrderBySortOrderAsc(reviewId)
                .stream()
                .map(img -> ReviewImageResponseDto.builder()
                        .id(img.getId())
                        .originName(img.getOriginName())
                        .storedUrl(img.getStoredUrl())
                        .sortOrder(img.getSortOrder())
                        .build())
                .toList();

        // 4. [추가] 해당 리뷰의 모든 댓글 조회
        List<ReviewComment> comments = reviewCommentRepository.findAllByReviewIdAndIsDeletedFalse(reviewId);

        List<CommentResponseDto> commentTree = convertToTreeStructure(comments);

        // 6. 최종 DTO 조립 및 반환
        return ReviewResponseDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .userId(user.getId())
                .authorAccountId(user.getUsername())
                .planId(review.getPlanId())
                .rating(review.getRating())
                .viewCount(review.getViewCount())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .images(imageDtos)
                .comments(commentTree)
                .build();
    }

    /**
     * [3] 리뷰 수정
     * 설명: 기존 내용을 변경하고, 이미지는 전체 삭제 후 새로 등록하는 방식을 사용합니다.
     */
    @Transactional
    public Long updateReview(Long reviewId, ReviewSaveRequestDto dto) {

        // 1. 수정할 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID : " + reviewId));

        // 2. 작성자 본인 확인 (팀원의 User PK ID 비교)
        if (!review.getUser().getId().equals(dto.getUserId())) {
            throw new IllegalStateException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        // 3. 본문 내용 수정 (엔티티의 update 메서드 호출)
        review.update(
                dto.getTitle(),
                dto.getContent(),
                dto.getRating(),
                dto.getIsPublic());

        // 4. 이미지 교체 로직
        // 기존 이미지를 DB에서 모두 삭제합니다.
        List<ReviewImage> oldImages = reviewImageRepository.findAllByReviewIdOrderBySortOrderAsc(reviewId);
        reviewImageRepository.deleteAll(oldImages);

        // 새 이미지가 들어왔다면 다시 등록합니다.
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (ReviewImageRequestDto imageRequest : dto.getImages()) {
                ReviewImage newImage = ReviewImage.builder()
                        .review(review)
                        .originName(imageRequest.getOriginName())
                        .storedUrl(imageRequest.getStoredUrl())
                        .sortOrder(imageRequest.getSortOrder())
                        .build();
                reviewImageRepository.save(newImage);
            }
        }

        return review.getId();
    }

    /**
     * [4] 리뷰 삭제 (Soft Delete)
     * 설명: DB에서 데이터를 삭제하지 않고, 조회되지 않도록 상태만 true로 변경합니다.
     */
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {

        // 1. 삭제할 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID: " + reviewId));

        // 2. 작성자 본인 확인
        // 현재 로그인한 유저(userId)가 리뷰 작성자(review.getUser().getId())와 같은지 체크

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalStateException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        // 3. 소프트 삭제 실행
        review.delete();
    }

    /**
     * [Admin] 전체 리뷰 목록 조회 (삭제된 리뷰 포함)
     */
    public Page<ReviewResponseDto> getAdminReviewList(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);

        return reviews.map(review -> {
            System.out.println("==============================");
            System.out.println("리뷰ID: " + review.getId() + ", 별점: " + review.getRating());
            System.out.println("==============================");
            String thumbnail = reviewImageRepository.findAllByReviewIdOrderBySortOrderAsc(review.getId())
                    .stream()
                    .findFirst()
                    .map(ReviewImage::getStoredUrl)
                    .orElse(null);

            return ReviewResponseDto.builder()
                    .id(review.getId())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .userId(review.getUser().getId())
                    .authorAccountId(review.getUser().getUsername())
                    .rating(review.getRating())
                    .viewCount(review.getViewCount())
                    .isPublic(review.getIsPublic())
                    .isDeleted(review.getIsDeleted())
                    .thumbnailUrl(thumbnail)
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .build();
        });
    }

    /**
     * [Admin] 공개/비공개 토글
     */
    @Transactional
    public void toggleReviewVisibility(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID: " + reviewId));
        review.setIsPublic(!review.getIsPublic());
    }

    /**
     * [Admin] 강제 삭제 (작성자 체크 없음)
     */
    @Transactional
    public void adminDeleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다. ID: " + reviewId));
        review.delete();
    }

    /**
     * [도움 메서드] 평면 리스트를 트리 구조(계층형)로 변환
     */
    private List<CommentResponseDto> convertToTreeStructure(List<ReviewComment> comments) {
        List<CommentResponseDto> result = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();

        // 1. 모든 댓글을 DTO로 변환하여 맵에 저장
        comments.forEach(c -> {
            CommentResponseDto dto = CommentResponseDto.builder()
                    .id(c.getId())
                    .userId(c.getUser().getId())
                    .authorAccountId(c.getUser().getUsername())
                    .content(c.getContent())
                    .parentId(c.getParent() != null ? c.getParent().getId() : null)
                    .isSecret(c.getIsSecret())
                    .isDeleted(c.getIsDeleted())
                    .createdAt(c.getCreatedAt())
                    .build();
            map.put(dto.getId(), dto);
        });

        // 2. 부모-자식 관계 연결
        comments.forEach(c -> {
            CommentResponseDto dto = map.get(c.getId());
            if (c.getParent() != null) {
                CommentResponseDto parentDto = map.get(c.getParent().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            } else {
                result.add(dto);
            }
        });

        return result;
    }
}
