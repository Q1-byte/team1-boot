package com.example.jpa.domain.point.dto;

import lombok.Getter;
import lombok.Setter;

public class PointDto {

    @Getter @Setter
    public static class Request {
        private Long userId;
        private Integer amount;
        private String description;
    }
}