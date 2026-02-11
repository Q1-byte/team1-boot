package com.example.jpa.domain.plan.dto;

import com.example.jpa.domain.accommodation.dto.AccommodationDto;
import com.example.jpa.domain.activity.dto.ActivityDto;
import com.example.jpa.domain.ticket.dto.TicketDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RandomMatchResponseDto {

    private TravelPlanResponseDto plan;

    // 매칭된 숙소 1건
    private AccommodationDto accommodation;

    // 매칭된 액티비티 N건
    private List<ActivityDto> activities;

    // 매칭된 티켓 N건
    private List<TicketDto> tickets;

    // 예상 총 비용 (숙소 × 박수 + 액티비티 합 + 티켓 합)
    private Integer estimatedTotalPrice;
}
