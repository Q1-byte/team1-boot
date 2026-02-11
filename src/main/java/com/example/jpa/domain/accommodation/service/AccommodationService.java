package com.example.jpa.domain.accommodation.service;

import com.example.jpa.domain.accommodation.entity.Accommodation;
import com.example.jpa.domain.accommodation.repository.AccommodationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public List<Accommodation> findByRegion(Long regionId) {
        return accommodationRepository.findByRegionIdAndIsActiveTrue(regionId);
    }

    public List<Accommodation> findByRegionAndPrice(Long regionId, Integer minPrice, Integer maxPrice) {
        return accommodationRepository.findByRegionIdAndPricePerNightBetweenAndIsActiveTrue(
                regionId, minPrice, maxPrice);
    }

    public List<Accommodation> findByRegionAndType(Long regionId, String type) {
        return accommodationRepository.findByRegionIdAndTypeAndIsActiveTrue(regionId, type);
    }

    public List<Accommodation> findByRegionAndPriceAndKeyword(Long regionId, Integer minPrice, Integer maxPrice, String keyword) {
        return accommodationRepository.findByRegionAndPriceAndKeyword(regionId, minPrice, maxPrice, keyword);
    }

    /**
     * 랜덤 매칭: 지역 + 가격 범위 조건으로 필터 후 랜덤 1건 반환
     */
    public Accommodation findRandomMatch(Long regionId, Integer minPrice, Integer maxPrice) {
        List<Accommodation> candidates = accommodationRepository
                .findByRegionIdAndPricePerNightBetweenAndIsActiveTrue(regionId, minPrice, maxPrice);
        if (candidates.isEmpty()) {
            // 가격 조건 없이 지역만으로 재시도
            candidates = accommodationRepository.findByRegionIdAndIsActiveTrue(regionId);
        }
        if (candidates.isEmpty()) return null;
        Collections.shuffle(candidates);
        return candidates.get(0);
    }

    /**
     * 랜덤 매칭: 지역 + 가격 + 키워드 조건으로 필터 후 랜덤 1건 반환
     */
    public Accommodation findRandomMatchWithKeyword(Long regionId, Integer minPrice, Integer maxPrice, String keyword) {
        List<Accommodation> candidates = accommodationRepository
                .findByRegionAndPriceAndKeyword(regionId, minPrice, maxPrice, keyword);
        if (candidates.isEmpty()) {
            candidates = accommodationRepository.findByRegionIdAndPricePerNightBetweenAndIsActiveTrue(
                    regionId, minPrice, maxPrice);
        }
        if (candidates.isEmpty()) {
            candidates = accommodationRepository.findByRegionIdAndIsActiveTrue(regionId);
        }
        if (candidates.isEmpty()) return null;
        Collections.shuffle(candidates);
        return candidates.get(0);
    }

    public Accommodation findOne(Long id) {
        return accommodationRepository.findById(id).orElseThrow();
    }

    public List<Accommodation> findAll() {
        return accommodationRepository.findAll();
    }
}
