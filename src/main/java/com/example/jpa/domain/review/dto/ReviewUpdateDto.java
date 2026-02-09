package com.example.jpa.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewUpdateDto {

    private String title;
    private String content;
    private Integer rating;
    private Boolean isPublic;
    private List<Long> deleteImageIds;

}
