package com.example.jpa.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class PlanDto {

    @Getter @Setter
    public static class Request {
        private String name;
        private Integer price;
        private String description;
    }

    @Getter @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private Integer price;
        private String description;
    }
}