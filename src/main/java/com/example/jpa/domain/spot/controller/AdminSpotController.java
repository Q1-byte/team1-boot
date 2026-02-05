package com.example.jpa.domain.spot.controller;

import com.example.jpa.domain.spot.dto.SpotDto;
import com.example.jpa.domain.spot.service.SpotService;
import com.example.jpa.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/spots")
@RequiredArgsConstructor // 생성자를 직접 쓰지 말고 이걸 다시 살립시다!
public class AdminSpotController {

    private final SpotService spotService;
    private final DataService dataService;

    // 기존 수동 추가 기능
    @PostMapping
    public ResponseEntity<Void> addSpot(@RequestBody SpotDto.Request request) {
        // spotService.createSpot(request);
        return ResponseEntity.ok().build();
    }

    // 전국 API 데이터 수집 시작 기능 추가
    @GetMapping("/init")
    public ResponseEntity<String> initApiData() {
        dataService.fetchAndDistributeData();
        return ResponseEntity.ok("전국 관광지 데이터 수집이 시작되었습니다.");
    }
}