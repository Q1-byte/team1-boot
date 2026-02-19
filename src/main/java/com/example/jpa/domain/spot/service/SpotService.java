package com.example.jpa.domain.spot.service;

import com.example.jpa.domain.region.entity.Region;
import com.example.jpa.domain.region.repository.RegionRepository;
import com.example.jpa.domain.spot.dto.SpotDto;
import com.example.jpa.domain.spot.entity.Spot;
import com.example.jpa.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final RegionRepository regionRepository;

    // ───────── 어드민 CRUD ─────────

    @Transactional(readOnly = true)
    public Page<SpotDto.AdminResponse> getSpotList(String keyword, Boolean isActive, Pageable pageable) {
        return spotRepository.searchSpots(
                (keyword != null && !keyword.isBlank()) ? keyword : null,
                isActive,
                pageable
        ).map(this::toAdminResponse);
    }

    @Transactional(readOnly = true)
    public SpotDto.AdminResponse getSpot(Long id) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행지입니다. id=" + id));
        return toAdminResponse(spot);
    }

    @Transactional
    public SpotDto.AdminResponse createSpot(SpotDto.Request request) {
        Spot spot = Spot.builder()
                .apiId("MANUAL_" + System.currentTimeMillis())
                .name(request.getName())
                .address(request.getAddress())
                .description(request.getDescription())
                .category(request.getCategory())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .imageUrl(request.getImageUrl())
                .level(request.getLevel())
                .regionId(request.getRegionId())
                .isActive(true)
                .build();
        return toAdminResponse(spotRepository.save(spot));
    }

    @Transactional
    public SpotDto.AdminResponse updateSpot(Long id, SpotDto.UpdateRequest request) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행지입니다. id=" + id));
        spot.update(request.getName(), request.getAddress(), request.getDescription(),
                request.getCategory(), request.getLatitude(), request.getLongitude(),
                request.getImageUrl(), request.getLevel(), request.getIsActive(), request.getRegionId());
        return toAdminResponse(spot);
    }

    @Transactional
    public void deleteSpot(Long id) {
        if (!spotRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 여행지입니다. id=" + id);
        }
        spotRepository.deleteById(id);
    }

    @Transactional
    public SpotDto.AdminResponse toggleActive(Long id) {
        Spot spot = spotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행지입니다. id=" + id));
        spot.toggleActive();
        return toAdminResponse(spot);
    }

    // ───────── API 데이터 수집용 ─────────

    @Transactional
    public void registerSpot(String apiId, String title, String addr, String mapx, String mapy, String imageUrl, String categoryId, String areaCode) {

        // 1. apiId(contentid)로 이미 저장된 데이터인지 확인 (중복 방지 핵심!)
        if (spotRepository.existsByApiId(apiId)) {
            return;
        }

        // 2. 위도/경도 변환
        Double lon = (mapx == null || mapx.isEmpty()) ? 0.0 : Double.parseDouble(mapx);
        Double lat = (mapy == null || mapy.isEmpty()) ? 0.0 : Double.parseDouble(mapy);

        // 3. 엔티티 생성 (새로 만든 테이블 컬럼에 맞춤)
        Spot spot = Spot.builder()
                .apiId(apiId)
                .name(title)
                .address(addr)
                .longitude(lon)
                .latitude(lat)
                .imageUrl(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : null)
                .category(categoryId)
                .regionId(regionRepository.findByAreaCode(areaCode).map(Region::getId).orElse(null))
                .description("API 연동 데이터")
                .isActive(true)
                .build();

        // 4. DB 저장
        spotRepository.save(spot);
    }

    // ───────── 내부 변환 ─────────

    private SpotDto.AdminResponse toAdminResponse(Spot spot) {
        return SpotDto.AdminResponse.builder()
                .id(spot.getId())
                .name(spot.getName())
                .address(spot.getAddress())
                .imageUrl(spot.getImageUrl())
                .category(spot.getCategory())
                .latitude(spot.getLatitude())
                .longitude(spot.getLongitude())
                .description(spot.getDescription())
                .level(spot.getLevel())
                .isActive(spot.getIsActive())
                .regionId(spot.getRegionId())
                .createdAt(spot.getCreatedAt())
                .build();
    }
}