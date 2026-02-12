package com.example.jpa.domain.region.controller;

import com.example.jpa.domain.region.entity.Region;
import com.example.jpa.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    // 시/도 목록 (부모 지역)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getParentRegions() {
        List<Region> regions = regionService.findParentRegions();
        List<Map<String, Object>> result = regions.stream()
                .map(r -> Map.<String, Object>of(
                        "id", r.getId(),
                        "name", r.getName(),
                        "areaCode", r.getAreaCode() != null ? r.getAreaCode() : ""
                ))
                .toList();
        return ResponseEntity.ok(result);
    }

    // 시/군/구 목록 (자식 지역)
    @GetMapping("/{parentId}/sub")
    public ResponseEntity<List<Map<String, Object>>> getSubRegions(@PathVariable Long parentId) {
        List<Region> subRegions = regionService.findSubRegions(parentId);
        List<Map<String, Object>> result = subRegions.stream()
                .map(r -> Map.<String, Object>of(
                        "id", r.getId(),
                        "name", r.getName(),
                        "sigunguCode", r.getSigunguCode() != null ? r.getSigunguCode() : ""
                ))
                .toList();
        return ResponseEntity.ok(result);
    }
}
