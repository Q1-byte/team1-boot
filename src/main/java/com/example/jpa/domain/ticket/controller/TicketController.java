package com.example.jpa.domain.ticket.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.ticket.dto.TicketDto;
import com.example.jpa.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketDto>>> getAll(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer maxPrice) {

        List<TicketDto> list;

        if (regionId != null && maxPrice != null && category != null) {
            // 지역 + 가격 + 카테고리 필터
            list = ticketService.findByRegionAndMaxPrice(regionId, maxPrice)
                    .stream()
                    .filter(t -> t.getCategory().equals(category))
                    .map(TicketDto::fromEntity).toList();
        } else if (regionId != null && maxPrice != null) {
            // 지역 + 가격 필터
            list = ticketService.findByRegionAndMaxPrice(regionId, maxPrice)
                    .stream().map(TicketDto::fromEntity).toList();
        } else if (regionId != null && category != null) {
            list = ticketService.findByRegionAndCategory(regionId, category)
                    .stream().map(TicketDto::fromEntity).toList();
        } else if (regionId != null) {
            list = ticketService.findByRegion(regionId)
                    .stream().map(TicketDto::fromEntity).toList();
        } else {
            list = ticketService.findAll()
                    .stream().map(TicketDto::fromEntity).toList();
        }

        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketDto>> getOne(@PathVariable Long id) {
        TicketDto dto = TicketDto.fromEntity(ticketService.findOne(id));
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
