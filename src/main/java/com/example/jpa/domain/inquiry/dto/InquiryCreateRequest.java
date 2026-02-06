package com.example.jpa.domain.inquiry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryCreateRequest {

    @NotBlank(message = "카테고리를 선택해주세요")
    private String category;

    @NotBlank(message = "제목을 입력해주세요")
    @Size(min = 5, max = 200, message = "제목은 5자 이상 200자 이하로 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @Builder.Default
    private Boolean isSecret = false;
}
