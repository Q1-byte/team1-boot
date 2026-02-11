package com.example.jpa.domain.ticket.dto;

import com.example.jpa.domain.ticket.entity.Ticket;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
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
    private LocalDate availableFrom;
    private LocalDate availableTo;

    public static TicketDto fromEntity(Ticket entity) {
        return TicketDto.builder()
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
                .availableFrom(entity.getAvailableFrom())
                .availableTo(entity.getAvailableTo())
                .build();
    }
}
