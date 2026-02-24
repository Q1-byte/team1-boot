package com.example.jpa.domain.accommodation.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.accommodation.dto.AccommodationDto;
import com.example.jpa.domain.accommodation.service.AccommodationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AccommodationDto>>> getAll(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer maxPrice) {

        List<AccommodationDto> list;

        if (regionId != null && maxPrice != null && type != null) {
            // 지역 + 가격 + 타입 필터
            list = accommodationService.findByRegionAndType(regionId, type)
                    .stream()
                    .filter(a -> a.getPricePerNight() <= maxPrice)
                    .map(AccommodationDto::fromEntity).toList();
        } else if (regionId != null && maxPrice != null) {
            // 지역 + 가격 필터 (0원 ~ maxPrice)
            list = accommodationService.findByRegionAndPrice(regionId, 0, maxPrice)
                    .stream().map(AccommodationDto::fromEntity).toList();
        } else if (regionId != null && type != null) {
            list = accommodationService.findByRegionAndType(regionId, type)
                    .stream().map(AccommodationDto::fromEntity).toList();
        } else if (regionId != null) {
            list = accommodationService.findByRegion(regionId)
                    .stream().map(AccommodationDto::fromEntity).toList();
        } else {
            list = accommodationService.findAll()
                    .stream().map(AccommodationDto::fromEntity).toList();
        }

        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccommodationDto>> getOne(@PathVariable Long id) {
        AccommodationDto dto = AccommodationDto.fromEntity(accommodationService.findOne(id));
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
