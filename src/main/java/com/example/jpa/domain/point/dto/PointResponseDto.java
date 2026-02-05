package com.example.jpa.domain.point.dto;

import com.example.jpa.domain.point.entity.Point;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointResponseDto {

    private Integer id;
    private Integer amount;
    private String description;
    private LocalDateTime createdAt;

    public static PointResponseDto fromEntity(Point point) {
        return PointResponseDto.builder()
                .id(point.getId())
                .amount(point.getAmount())
                .description(point.getDescription())
                .createdAt(point.getCreatedAt())
                .build();
    }
}
