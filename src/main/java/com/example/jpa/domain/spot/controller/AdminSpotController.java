package com.example.jpa.domain.spot.controller;

import com.example.jpa.domain.spot.dto.SpotDto;
import com.example.jpa.domain.spot.service.SpotService;
import com.example.jpa.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/spots")
@RequiredArgsConstructor
public class AdminSpotController {

    private final DataService dataService;
    private final SpotService spotService;

    // 목록 조회 (페이지네이션 + 검색 + 필터)
    @GetMapping
    public ResponseEntity<Page<SpotDto.AdminResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isActive,
            Pageable pageable) {
        return ResponseEntity.ok(spotService.getSpotList(keyword, isActive, pageable));
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(spotService.getSpot(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 수동 등록
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SpotDto.Request request) {
        return ResponseEntity.ok(spotService.createSpot(request));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SpotDto.UpdateRequest request) {
        try {
            return ResponseEntity.ok(spotService.updateSpot(id, request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            spotService.deleteSpot(id);
            return ResponseEntity.ok("삭제 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 활성화/비활성화 토글
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<?> toggle(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(spotService.toggleActive(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    // 전국 API 데이터 수집 시작 (비동기)
    @GetMapping("/init")
    public ResponseEntity<String> initApiData() {
        dataService.fetchAndDistributeData();
        return ResponseEntity.ok("전국 관광지 데이터 수집이 시작되었습니다.");
    }
}