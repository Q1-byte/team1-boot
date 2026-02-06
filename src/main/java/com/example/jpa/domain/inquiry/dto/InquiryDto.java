package com.example.jpa.domain.inquiry.dto;

import com.example.jpa.domain.inquiry.entity.Inquiry;
import com.example.jpa.domain.inquiry.entity.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryDto {

    private Long id;
    private Long userId;
    private String writerName;
    private String category;
    private String categoryName;
    private String title;
    private String content;
    private String answer;
    private InquiryStatus status;
    private String statusDescription;
    private Boolean isSecret;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
    private String answeredByName;
    private List<InquiryFileDto> files;

    // Entity -> DTO 변환
    public static InquiryDto fromEntity(Inquiry inquiry) {
        return InquiryDto.builder()
                .id(inquiry.getId())
                .userId(inquiry.getWriter().getId())
                .writerName(inquiry.getWriter().getUsername())
                .category(inquiry.getCategory())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .answer(inquiry.getAnswer())
                .status(inquiry.getStatus())
                .statusDescription(inquiry.getStatus().getDescription())
                .isSecret(inquiry.getIsSecret())
                .createdAt(inquiry.getCreatedAt())
                .answeredAt(inquiry.getAnsweredAt())
                .answeredByName(inquiry.getAnsweredBy() != null ? inquiry.getAnsweredBy().getUsername() : null)
                .files(inquiry.getFiles() != null ?
                       inquiry.getFiles().stream()
                               .map(InquiryFileDto::fromEntity)
                               .collect(Collectors.toList()) : null)
                .build();
    }

    // 목록용 DTO (간략 정보만)
    public static InquiryDto fromEntityForList(Inquiry inquiry) {
        return InquiryDto.builder()
                .id(inquiry.getId())
                .userId(inquiry.getWriter().getId())
                .writerName(inquiry.getWriter().getUsername())
                .category(inquiry.getCategory())
                .title(inquiry.getTitle())
                .status(inquiry.getStatus())
                .statusDescription(inquiry.getStatus().getDescription())
                .isSecret(inquiry.getIsSecret())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }
}
