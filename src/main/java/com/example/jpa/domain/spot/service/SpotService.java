package com.example.jpa.domain.spot.service;

import com.example.jpa.domain.region.entity.Region;
import com.example.jpa.domain.region.repository.RegionRepository;
import com.example.jpa.domain.spot.entity.Spot;
import com.example.jpa.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;
    private final RegionRepository regionRepository;

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
}