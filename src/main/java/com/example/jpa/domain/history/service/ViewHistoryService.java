package com.example.jpa.domain.history.service;

import com.example.jpa.domain.history.dto.ViewHistoryResponseDto;
import com.example.jpa.domain.history.entity.ViewHistory;
import com.example.jpa.domain.history.repository.ViewHistoryRepository;
import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import com.example.jpa.domain.plan.entity.TravelPlan;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewHistoryService {

    private final ViewHistoryRepository viewHistoryRepository;
    private final TravelPlanRepository travelPlanRepository;

    @Transactional
    public void saveViewHistory(Long userId, Long planId) {
        ViewHistory history = ViewHistory.builder()
                .userId(userId)
                .planId(planId)
                .build();
        viewHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<ViewHistoryResponseDto> getRecentViewedPlans(Long userId) {
        List<ViewHistory> histories = viewHistoryRepository.findTop3ByUserIdOrderByViewedAtDesc(userId);
        List<ViewHistoryResponseDto> result = new ArrayList<>();

        for (ViewHistory history : histories) {
            TravelPlan plan = travelPlanRepository.findById(history.getPlanId()).orElse(null);
            if (plan != null) {
                result.add(ViewHistoryResponseDto.fromEntityWithPlan(history, TravelPlanResponseDto.fromEntity(plan)));
            } else {
                result.add(ViewHistoryResponseDto.fromEntity(history));
            }
        }

        return result;
    }
}
