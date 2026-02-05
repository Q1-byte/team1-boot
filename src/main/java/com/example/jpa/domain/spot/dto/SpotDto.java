package com.example.jpa.domain.spot.dto;

import lombok.Getter;
import lombok.Setter;

public class SpotDto {
    @Getter @Setter
    public static class Request {
        private String name;
        private String address;
        private String description;
    }
}