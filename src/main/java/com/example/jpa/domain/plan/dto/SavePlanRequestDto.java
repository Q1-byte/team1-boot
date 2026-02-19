package com.example.jpa.domain.plan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SavePlanRequestDto {

    private Long userId;
    private String regionName;
    private String startDate;   // "YYYY-MM-DD"
    private String endDate;     // "YYYY-MM-DD"
    private Integer peopleCount;
    private List<SpotEntry> spots;

    @Getter
    @Setter
    public static class SpotEntry {
        private Long spotId;
        private Integer day;
    }
}
