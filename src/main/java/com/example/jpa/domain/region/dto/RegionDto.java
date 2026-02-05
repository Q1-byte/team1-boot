package com.example.jpa.domain.region.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class RegionDto {

    @Getter @Setter
    public static class Request {
        private String cityName;
        private String areaCode;
    }

    @Getter @AllArgsConstructor
    public static class Response {
        private Long id;
        private String cityName;
        private String areaCode;
    }
}