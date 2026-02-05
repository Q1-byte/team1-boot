package com.example.jpa.domain.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class EventDto {

    @Getter @Setter
    public static class Request {
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Getter @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private LocalDateTime createdAt;
    }
}