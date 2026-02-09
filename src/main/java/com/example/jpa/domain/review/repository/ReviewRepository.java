package com.example.jpa.domain.review.repository;

import com.example.jpa.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 1. 최신순 정렬 (엔티티의 @CreationTimestamp createdAt 필드와 매칭 - 이상 없음)
    List<Review> findAllByIsDeletedFalseOrderByCreatedAtDesc();

    // 2. 조회수순 정렬 (엔티티의 viewCount 필드와 매칭 - 이상 없음)
    List<Review> findAllByIsDeletedFalseOrderByViewCountDesc();

    // 3. [수정] 기존 findAllByUserId -> findAllByByUserId (X)
    // 엔티티가 'User user' 객체를 들고 있으므로 'User_Id'라고 명시해야 JPA가 user 객체 내부의 id를 찾습니다.
    List<Review> findAllByUserIdAndIsDeletedFalse(Long userId);

    // ReviewRepository.java 안에 추가
    Page<Review> findAllByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
}