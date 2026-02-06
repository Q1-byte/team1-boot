package com.example.jpa.domain.inquiry.dto;

import com.example.jpa.domain.inquiry.entity.InquiryFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryFileDto {

    private Long id;
    private String originalName;
    private String storedName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private LocalDateTime createdAt;

    public static InquiryFileDto fromEntity(InquiryFile file) {
        return InquiryFileDto.builder()
                .id(file.getId())
                .originalName(file.getOriginalName())
                .storedName(file.getStoredName())
                .filePath(file.getFilePath())
                .fileSize(file.getFileSize())
                .contentType(file.getContentType())
                .createdAt(file.getCreatedAt())
                .build();
    }
}
