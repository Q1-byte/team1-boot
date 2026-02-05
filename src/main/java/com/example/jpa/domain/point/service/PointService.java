package com.example.jpa.domain.point.service;

import com.example.jpa.domain.history.service.HistoryService;
import com.example.jpa.domain.point.dto.PointDto;
import com.example.jpa.domain.point.entity.Point;
import com.example.jpa.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointRepository pointRepository;
    private final HistoryService historyService; // 반드시 final 확인!

    @Transactional
    public void addPoint(PointDto.Request request) {
        // 1. 포인트 저장
        Point point = Point.builder()
                .userId(request.getUserId())
                .amount(request.getAmount())
                .description(request.getDescription())
                .build();

        pointRepository.save(point);

        // 2. 히스토리에 기록 남기기 (아까 만든 log 메서드 활용)
        String logMsg = String.format("%s (금액: %d)", request.getDescription(), request.getAmount());
        historyService.log("POINT_EVENT", logMsg, request.getUserId().intValue());
    }
}