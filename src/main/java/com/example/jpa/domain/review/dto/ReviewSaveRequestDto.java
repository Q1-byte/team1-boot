package com.example.jpa.domain.review.dto;

import com.example.jpa.domain.review.entity.Review;
import com.example.jpa.domain.user.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewSaveRequestDto {
    private String title;
    private String content;
    private Long userId;
    private Long planId;
    private Integer rating;
    private Boolean isPublic;

    // [추가] 서비스에서 .getImages()를 쓸 수 있도록 필드 추가
    private List<ReviewImageRequestDto> images;

    public Review toEntity(User user) {
        return Review.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .planId(this.planId)
                .rating(this.rating)
                .isPublic(this.isPublic)
                .build();
    }
}
