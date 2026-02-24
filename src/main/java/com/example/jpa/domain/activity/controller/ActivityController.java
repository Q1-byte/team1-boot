package com.example.jpa.domain.activity.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.activity.dto.ActivityDto;
import com.example.jpa.domain.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityDto>>> getAll(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer maxPrice) {

        List<ActivityDto> list;

        if (regionId != null && maxPrice != null && category != null) {
            // 지역 + 가격 + 카테고리 필터
            list = activityService.findByRegionAndMaxPrice(regionId, maxPrice)
                    .stream()
                    .filter(a -> a.getCategory().equals(category))
                    .map(ActivityDto::fromEntity).toList();
        } else if (regionId != null && maxPrice != null) {
            // 지역 + 가격 필터
            list = activityService.findByRegionAndMaxPrice(regionId, maxPrice)
                    .stream().map(ActivityDto::fromEntity).toList();
        } else if (regionId != null && category != null) {
            list = activityService.findByRegionAndCategory(regionId, category)
                    .stream().map(ActivityDto::fromEntity).toList();
        } else if (regionId != null) {
            list = activityService.findByRegion(regionId)
                    .stream().map(ActivityDto::fromEntity).toList();
        } else {
            list = activityService.findAll()
                    .stream().map(ActivityDto::fromEntity).toList();
        }

        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ActivityDto>> getOne(@PathVariable Long id) {
        ActivityDto dto = ActivityDto.fromEntity(activityService.findOne(id));
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
