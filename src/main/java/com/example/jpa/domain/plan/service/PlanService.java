package com.example.jpa.domain.plan.service;

import com.example.jpa.domain.plan.dto.SavePlanRequestDto;
import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import com.example.jpa.domain.plan.entity.Plan;
import com.example.jpa.domain.plan.entity.TravelPlan;
import com.example.jpa.domain.plan.entity.TravelSpot;
import com.example.jpa.domain.plan.entity.PlanSpot;
import com.example.jpa.domain.plan.repository.PlanRepository;
import com.example.jpa.domain.plan.repository.PlanSpotRepository;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import com.example.jpa.domain.plan.repository.TravelSpotRepository;
import com.example.jpa.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final TravelSpotRepository travelSpotRepository;
    private final PlanSpotRepository planSpotRepository;
    private final RegionRepository regionRepository;

    /**
     * [관리자용] 모든 플랜 조회
     */
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    /**
     * [관리자용] 특정 플랜 상세 조회
     */
    public Plan findOne(Long id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 플랜이 존재하지 않습니다. ID: " + id));
    }

    /**
     * [관리자용] 플랜 저장/수정
     */
    @Transactional
    public Long savePlan(Plan plan) {
        return planRepository.save(plan).getId();
    }

    /**
     * [관리자용] 플랜 삭제
     */
    @Transactional
    public void delete(Long id) {
        planRepository.deleteById(id);
    }

    /**
     * [사용자용] 저장된 계획의 스팟 목록 교체 (PUT /plans/{id}/spots)
     * 기존 plan_spot 레코드를 모두 삭제하고 새 목록으로 재저장합니다.
     */
    @Transactional
    public void updatePlanSpots(Long planId, List<SavePlanRequestDto.SpotEntry> spots) {
        if (!travelPlanRepository.existsById(planId)) {
            throw new NoSuchElementException("해당 계획이 존재하지 않습니다. id=" + planId);
        }
        planSpotRepository.deleteByPlanId(planId);
        if (spots != null) {
            for (SavePlanRequestDto.SpotEntry entry : spots) {
                planSpotRepository.save(PlanSpot.builder()
                        .planId(planId)
                        .spotId(entry.getSpotId())
                        .day(entry.getDay())
                        .build());
            }
        }
    }

    /**
     * [사용자용] 계획 삭제
     */
    @Transactional
    public void deleteTravelPlan(Long planId) {
        if (!travelPlanRepository.existsById(planId)) {
            throw new NoSuchElementException("해당 계획이 존재하지 않습니다. id=" + planId);
        }
        planSpotRepository.deleteByPlanId(planId);
        travelPlanRepository.deleteById(planId);
    }

    /**
     * [사용자용] 저장된 계획 단건 조회 (schedule 포함)
     */
    public TravelPlanResponseDto getTravelPlan(Long planId) {
        TravelPlan plan = travelPlanRepository.findById(planId)
                .orElseThrow(() -> new NoSuchElementException("해당 계획이 존재하지 않습니다. id=" + planId));

        // plan_spot → TravelSpot 조회 → schedule 재조립
        Map<String, List<TravelSpot>> schedule = new LinkedHashMap<>();
        List<PlanSpot> planSpots = planSpotRepository.findByPlanIdOrderByDayAsc(planId);
        for (PlanSpot ps : planSpots) {
            String key = "Day " + ps.getDay();
            // ✅ findByIdWithKeywords: spotKeywords JOIN FETCH로 Lazy 직렬화 문제 방지
            travelSpotRepository.findByIdWithKeywords(ps.getSpotId())
                    .ifPresent(spot -> schedule.computeIfAbsent(key, k -> new ArrayList<>()).add(spot));
        }

        TravelPlanResponseDto dto = TravelPlanResponseDto.fromEntity(plan);
        dto.setSchedule(schedule.isEmpty() ? null : schedule);

        // regionId로 regionName 조회해서 region 필드에 설정
        if (plan.getRegionId() != null) {
            regionRepository.findById(plan.getRegionId())
                    .ifPresent(r -> dto.setRegion(r.getName()));
        }

        return dto;
    }

    /**
     * [사용자용] AI 추천 일정 저장
     */
    @Transactional
    public TravelPlanResponseDto saveTravelPlan(SavePlanRequestDto request, Long userId) {
        DateTimeFormatter formatter = request.getStartDate().contains("-")
                ? DateTimeFormatter.ISO_LOCAL_DATE
                : DateTimeFormatter.ofPattern("yyyy. M. d.");
        LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
        int durationDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        Long regionId = request.getRegionId() != null
                ? request.getRegionId()
                : regionRepository.findByName(request.getRegionName()).map(r -> r.getId()).orElse(null);

        TravelPlan plan = TravelPlan.builder()
                .userId(userId)
                .type("CUSTOM")
                .title(request.getRegionName() + " AI 맞춤 여행 일정")
                .regionId(regionId)
                .peopleCount(request.getPeopleCount() != null ? request.getPeopleCount() : 1)
                .travelDate(startDate)
                .durationDays(durationDays)
                .status("AI_SAVED")
                .build();

        TravelPlan saved = travelPlanRepository.save(plan);

        // spots 저장
        if (request.getSpots() != null) {
            for (SavePlanRequestDto.SpotEntry entry : request.getSpots()) {
                planSpotRepository.save(PlanSpot.builder()
                        .planId(saved.getId())
                        .spotId(entry.getSpotId())
                        .day(entry.getDay())
                        .build());
            }
        }

        return TravelPlanResponseDto.fromEntity(saved);
    }

    /**
     * 🚀 [사용자용] 키워드 기반 2박 3일 여행 코스 생성
     */
    public TravelPlanResponseDto createPlan(List<String> keywords, String region) {
        // 1. 카테고리별 데이터 가져오기 (식당 39, 관광지 14/12/28)
        List<TravelSpot> allRestaurants = new ArrayList<>(
                travelSpotRepository.findAllByKeywordsAndCategory(keywords, "39"));
        List<TravelSpot> attractions = new ArrayList<>(
                travelSpotRepository.findAllByKeywordsAndCategory(keywords, "14"));

        // 관광지 데이터 보강 (12번, 28번도 있다면 추가)
        attractions.addAll(travelSpotRepository.findAllByKeywordsAndCategory(keywords, "12"));
        attractions.addAll(travelSpotRepository.findAllByKeywordsAndCategory(keywords, "28"));

        // 2. 지역 필터링
        if (region != null && !region.isEmpty()) {
            allRestaurants = allRestaurants.stream().filter(s -> s.getAddress().contains(region))
                    .collect(Collectors.toCollection(ArrayList::new));
            attractions = attractions.stream().filter(s -> s.getAddress().contains(region))
                    .collect(Collectors.toCollection(ArrayList::new));

            // 3. 키워드 매칭으로 부족하면 같은 지역 전체에서 보충
            final int MIN_RESTAURANTS = 8;
            final int MIN_ATTRACTIONS = 8;

            if (allRestaurants.size() < MIN_RESTAURANTS) {
                Set<Long> existingIds = allRestaurants.stream().map(TravelSpot::getId).collect(Collectors.toSet());
                List<TravelSpot> fallbackRestaurants = travelSpotRepository.findByCategoryAndRegion("39", region)
                        .stream().filter(s -> !existingIds.contains(s.getId())).collect(Collectors.toList());
                Collections.shuffle(fallbackRestaurants);
                allRestaurants.addAll(fallbackRestaurants.subList(0,
                        Math.min(fallbackRestaurants.size(), MIN_RESTAURANTS - allRestaurants.size())));
            }

            if (attractions.size() < MIN_ATTRACTIONS) {
                Set<Long> existingIds = attractions.stream().map(TravelSpot::getId).collect(Collectors.toSet());
                List<TravelSpot> fallbackAttractions = new ArrayList<>();
                for (String cat : Arrays.asList("14", "12", "28")) {
                    fallbackAttractions.addAll(travelSpotRepository.findByCategoryAndRegion(cat, region));
                }
                fallbackAttractions = fallbackAttractions.stream().filter(s -> !existingIds.contains(s.getId()))
                        .collect(Collectors.toList());
                Collections.shuffle(fallbackAttractions);
                attractions.addAll(fallbackAttractions.subList(0,
                        Math.min(fallbackAttractions.size(), MIN_ATTRACTIONS - attractions.size())));
            }
        }

        // 3. ☕ 카페 세부 필터링 (식당 리스트에서 분리)
        List<String> cafeKeywords = Arrays.asList("카페", "커피", "디저트", "찻집", "베이커리");
        List<TravelSpot> cafes = allRestaurants.stream()
                .filter(s -> cafeKeywords.stream().anyMatch(k -> s.getName().contains(k)))
                .collect(Collectors.toCollection(ArrayList::new));

        // 카페를 제외한 순수 식당 리스트
        List<TravelSpot> restaurants = allRestaurants.stream()
                .filter(s -> !cafes.contains(s))
                .collect(Collectors.toCollection(ArrayList::new));

        // 랜덤 섞기
        Collections.shuffle(restaurants);
        Collections.shuffle(attractions);
        Collections.shuffle(cafes);

        Map<String, List<TravelSpot>> schedule = new LinkedHashMap<>();

        // 📅 Day 1: 점심 -> 관광지 -> 카페 -> 저녁 -> 야경(관광지)
        List<TravelSpot> day1 = new ArrayList<>();
        addSpot(day1, restaurants); // 점심
        addSpot(day1, attractions); // 관광지
        addSpot(day1, cafes); // 카페
        addSpot(day1, restaurants); // 저녁
        addSpot(day1, attractions); // 야경
        schedule.put("Day 1", sortByLocation(day1));

        // 📅 Day 2: 아침 -> 관광지1 -> 점심 -> 관광지2 -> 관광지3 -> 저녁 -> 야경(관광지)
        List<TravelSpot> day2 = new ArrayList<>();
        addSpot(day2, restaurants); // 아침
        addSpot(day2, attractions); // 관광지1
        addSpot(day2, restaurants); // 점심
        addSpot(day2, attractions); // 관광지2
        addSpot(day2, attractions); // 관광지3
        addSpot(day2, restaurants); // 저녁
        addSpot(day2, attractions); // 야경
        schedule.put("Day 2", sortByLocation(day2));

        // 📅 Day 3: 아침 -> 체크아웃 -> 관광지
        List<TravelSpot> day3 = new ArrayList<>();
        addSpot(day3, restaurants); // 아침
        addSpot(day3, attractions); // 마지막 관광지
        schedule.put("Day 3", sortByLocation(day3));

        return TravelPlanResponseDto.builder()
                .region(region)
                .selectedKeywords(keywords)
                .schedule(schedule)
                .build();
    }

    /**
     * 📍 안전하게 리스트에서 장소를 꺼내 추가하는 메서드
     */
    private void addSpot(List<TravelSpot> targetList, List<TravelSpot> sourceList) {
        if (sourceList != null && !sourceList.isEmpty()) {
            targetList.add(sourceList.remove(0));
        }
    }

    /**
     * 📍 Nearest Neighbor 알고리즘을 이용한 동선 정렬
     */
    private List<TravelSpot> sortByLocation(List<TravelSpot> spots) {
        if (spots.size() <= 1)
            return spots;

        List<TravelSpot> sorted = new ArrayList<>();
        sorted.add(spots.remove(0));

        while (!spots.isEmpty()) {
            TravelSpot last = sorted.get(sorted.size() - 1);
            TravelSpot closest = spots.stream()
                    .min(Comparator.comparingDouble(s -> calculateDistance(last, s)))
                    .orElse(spots.get(0));

            sorted.add(closest);
            spots.remove(closest);
        }
        return sorted;
    }

    /**
     * 📏 위경도 기반 거리 계산
     */
    private double calculateDistance(TravelSpot s1, TravelSpot s2) {
        if (s1.getMapy() == null || s2.getMapy() == null ||
                s1.getMapx() == null || s2.getMapx() == null)
            return 0;

        double latDiff = s1.getMapy() - s2.getMapy();
        double lonDiff = s1.getMapx() - s2.getMapx();
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
    }
}