package com.example.jpa.domain.inquiry.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InquiryAnswerRequest {

    @NotBlank(message = "답변 내용을 입력해주세요")
    private String answer;
}
