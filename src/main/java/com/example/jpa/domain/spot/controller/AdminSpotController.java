package com.example.jpa.domain.spot.controller;

import com.example.jpa.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/spots")
@RequiredArgsConstructor
public class AdminSpotController {

    private final DataService dataService;

    // 전국 API 데이터 수집 시작 (비동기)
    @GetMapping("/init")
    public ResponseEntity<String> initApiData() {
        dataService.fetchAndDistributeData();
        return ResponseEntity.ok("전국 관광지 데이터 수집이 시작되었습니다.");
    }
}