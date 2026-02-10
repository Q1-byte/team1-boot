package com.example.jpa.domain.spot.dto;

import lombok.*;

public class SpotDto {

    // 1. 프론트엔드에서 장소 등록 요청을 보낼 때 (기존 유지 + 필드 추가)
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String name;
        private String address;
        private String description;
        private String category;
        private Double latitude;
        private Double longitude;
        private String imageUrl;
    }

    // 2. 프론트엔드에 목록을 뿌려줄 때 (응답용)
    @Getter
    @Builder
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String address;
        private String imageUrl;
        private String category;
        private Double latitude;
        private Double longitude;
        private String description;
    }
}