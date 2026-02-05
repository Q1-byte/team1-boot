package com.example.jpa.domain.point.controller;

import com.example.jpa.domain.point.dto.PointDto;
import com.example.jpa.domain.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/points")
@RequiredArgsConstructor
public class AdminPointController {

    private final PointService pointService;

    @PostMapping // 관리자가 특정 사용자에게 포인트 지급/차감
    public ResponseEntity<Void> givePoint(@RequestBody PointDto.Request request) {
        pointService.addPoint(request);
        return ResponseEntity.ok().build();
    }
}