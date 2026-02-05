package com.example.jpa.domain.keyword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class KeywordDto {

    @Getter @Setter
    public static class Request {
        private String name;
        private Integer priority;
    }

    @Getter @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private Integer priority;
    }
}