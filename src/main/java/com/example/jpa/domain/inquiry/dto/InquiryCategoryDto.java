package com.example.jpa.domain.inquiry.dto;

import com.example.jpa.domain.inquiry.entity.InquiryCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryCategoryDto {

    private String code;
    private String name;
    private Integer displayOrder;

    public static InquiryCategoryDto fromEntity(InquiryCategory category) {
        return InquiryCategoryDto.builder()
                .code(category.getCode())
                .name(category.getName())
                .displayOrder(category.getDisplayOrder())
                .build();
    }
}
