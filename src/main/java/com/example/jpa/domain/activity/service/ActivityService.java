package com.example.jpa.domain.activity.service;

import com.example.jpa.domain.activity.entity.Activity;
import com.example.jpa.domain.activity.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> findByRegion(Long regionId) {
        return activityRepository.findByRegionIdAndIsActiveTrue(regionId);
    }

    public List<Activity> findByRegionAndMaxPrice(Long regionId, Integer maxPrice) {
        return activityRepository.findByRegionIdAndPriceLessThanEqualAndIsActiveTrue(regionId, maxPrice);
    }

    public List<Activity> findByRegionAndCategory(Long regionId, String category) {
        return activityRepository.findByRegionIdAndCategoryAndIsActiveTrue(regionId, category);
    }

    public List<Activity> findByRegionAndPriceAndKeyword(Long regionId, Integer maxPrice, String keyword) {
        return activityRepository.findByRegionAndPriceAndKeyword(regionId, maxPrice, keyword);
    }

    /**
     * 랜덤 매칭: 지역 + 예산 내 액티비티 중 랜덤 N건 반환
     */
    public List<Activity> findRandomMatches(Long regionId, Integer maxPrice, int count) {
        List<Activity> candidates = activityRepository
                .findByRegionIdAndPriceLessThanEqualAndIsActiveTrue(regionId, maxPrice);
        if (candidates.isEmpty()) {
            candidates = activityRepository.findByRegionIdAndIsActiveTrue(regionId);
        }
        if (candidates.isEmpty()) return List.of();
        List<Activity> shuffled = new ArrayList<>(candidates);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    /**
     * 랜덤 매칭: 지역 + 예산 + 키워드 조건으로 랜덤 N건 반환
     */
    public List<Activity> findRandomMatchesWithKeyword(Long regionId, Integer maxPrice, String keyword, int count) {
        List<Activity> candidates = activityRepository
                .findByRegionAndPriceAndKeyword(regionId, maxPrice, keyword);
        if (candidates.isEmpty()) {
            candidates = activityRepository.findByRegionIdAndPriceLessThanEqualAndIsActiveTrue(regionId, maxPrice);
        }
        if (candidates.isEmpty()) {
            candidates = activityRepository.findByRegionIdAndIsActiveTrue(regionId);
        }
        if (candidates.isEmpty()) return List.of();
        List<Activity> shuffled = new ArrayList<>(candidates);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    public Activity findOne(Long id) {
        return activityRepository.findById(id).orElseThrow();
    }

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }
}
