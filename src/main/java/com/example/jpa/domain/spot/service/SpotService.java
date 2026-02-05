package com.example.jpa.domain.spot.service;

import com.example.jpa.domain.spot.entity.Spot;
import com.example.jpa.domain.spot.repository.SpotRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;

    @Transactional
    public void saveSpotFromApi(JsonNode item) {
        String name = item.path("title").asText();

        if (!spotRepository.existsByName(name)) {
            // 지역 코드와 좌표 처리
            String areaCodeStr = item.path("areacode").asText();
            Long regionId = areaCodeStr.isEmpty() ? 1L : Long.parseLong(areaCodeStr);

            Spot spot = Spot.builder()
                    .name(name)
                    .address(item.path("addr1").asText())
                    .latitude(item.path("mapy").asDouble(0.0))
                    .longitude(item.path("mapx").asDouble(0.0))
                    .regionId(regionId)
                    .category(item.path("contenttypeid").asText())
                    .description("API 연동 데이터")
                    .build();

            spotRepository.save(spot);
        }
    }

        @Transactional
        public void registerSpot(String title, String addr, String mapx, String mapy) {
            // 1. 위도/경도 값이 비어있을 경우를 대비한 안전한 변환
            Double lon = (mapx == null || mapx.isEmpty()) ? 0.0 : Double.parseDouble(mapx);
            Double lat = (mapy == null || mapy.isEmpty()) ? 0.0 : Double.parseDouble(mapy);

            // 2. 엔티티 생성 (현재 Spot 엔티티 필드명: name, address, longitude, latitude)
            Spot spot = Spot.builder()
                    .name(title)
                    .address(addr)
                    .longitude(lon)
                    .latitude(lat)
                    .build();

            // 3. DB 저장
            spotRepository.save(spot);
        }
    }