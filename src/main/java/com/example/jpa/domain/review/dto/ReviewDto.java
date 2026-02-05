package com.example.jpa.domain.review.dto;

import lombok.*;

@Data
public class ReviewDto {

    @Getter @Setter
    @Builder // <--- 이게 있어야 .builder()를 쓸 수 있어요!
    @NoArgsConstructor // <--- 이거랑
    @AllArgsConstructor // <--- 이거까지 세트로 넣어주는 게 안전합니다.
    public static class Request {
        private Integer userId;
        private Integer spotId;
        private String content;
        private int rating;
    }
}