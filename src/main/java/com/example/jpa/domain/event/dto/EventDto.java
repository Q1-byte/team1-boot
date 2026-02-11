package com.example.jpa.domain.event.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private Long contentId;
    private String name;
    private String address;
    private String addr2;
    private String zipCode;
    private String tel;
    private String url; // imageUrl -> url
    private String description; // [추가] 설명글
    private String cat1;
    private String category; // cat2 -> category
    private String cat3;
    private String startDate;
    private String endDate;
    private Double mapX;
    private Double mapY;
}