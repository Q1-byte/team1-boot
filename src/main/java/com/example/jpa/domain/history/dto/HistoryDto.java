package com.example.jpa.domain.history.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class HistoryDto {

    @Getter @Setter
    public static class Request {
        private String content;
        private String type;
    }

    @Getter @AllArgsConstructor
    public static class Response {
        private Long id;
        private String content;
        private String type;
        private LocalDateTime createdAt;
    }
}