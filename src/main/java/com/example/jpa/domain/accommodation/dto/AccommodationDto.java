package com.example.jpa.domain.accommodation.dto;

import com.example.jpa.domain.accommodation.entity.Accommodation;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccommodationDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long regionId;
    private Double latitude;
    private Double longitude;
    private Integer pricePerNight;
    private String type;
    private String keywords;

    public static AccommodationDto fromEntity(Accommodation entity) {
        return AccommodationDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .imageUrl(entity.getImageUrl())
                .regionId(entity.getRegionId())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .pricePerNight(entity.getPricePerNight())
                .type(entity.getType())
                .keywords(entity.getKeywords())
                .build();
    }
}
