package com.example.jpa.domain.rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class RuleDto {

    @Getter @Setter
    public static class Request {
        private String title;
        private String content;
    }

    @Getter @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String content;
    }
}