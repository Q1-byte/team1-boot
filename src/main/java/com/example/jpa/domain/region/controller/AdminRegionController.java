package com.example.jpa.domain.region.controller;

import com.example.jpa.domain.region.dto.RegionDto;
import com.example.jpa.domain.region.entity.Region;
import com.example.jpa.domain.region.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/regions")
public class AdminRegionController {
    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<Region>> list() {
        return ResponseEntity.ok(regionService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody RegionDto.Request request) {
        Region region = new Region();


        region.setAreaCode(request.getAreaCode());
        region.setCityName(request.getCityName());

        return ResponseEntity.ok(regionService.saveRegion(region));
    }
}