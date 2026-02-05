package com.example.jpa.domain.inquiry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryDto {
    private Integer id;
    private String title;
    private String content;
    private String answer;
    private String status;
    private String writerName; // Member 엔티티에서 이름을 가져와서 담음
    private LocalDateTime regDate;
}