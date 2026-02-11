package com.example.jpa.domain.activity.dto;

import com.example.jpa.domain.activity.entity.Activity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long regionId;
    private Double latitude;
    private Double longitude;
    private Integer price;
    private String category;
    private String keywords;
    private Integer durationMinutes;

    public static ActivityDto fromEntity(Activity entity) {
        return ActivityDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .regionId(entity.getRegionId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .keywords(entity.getKeywords())
                .durationMinutes(entity.getDurationMinutes())
                .build();
    }
}
