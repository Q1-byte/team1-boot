package com.example.jpa.domain.content.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {
    private Integer id;
    private String title;
    private String body;
    private String category;
    private int viewCount;
    private LocalDateTime regDate;
}