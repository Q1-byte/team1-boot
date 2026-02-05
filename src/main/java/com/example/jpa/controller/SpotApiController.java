package com.example.jpa.controller; // 1. 사진 속 폴더 경로와 일치해야 함

import com.example.jpa.domain.spot.entity.Spot;
import com.example.jpa.domain.spot.repository.SpotRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpotApiController {

    private final SpotRepository spotRepository;

    /**
     * DataService에서 넘겨준 JSON 아이템 하나를 분석해서 DB에 저장합니다.
     * 메서드 이름이 DataService에서 호출하는 이름과 정확히 같아야 합니다.
     */
    @Transactional
    public void saveSpotFromApi(JsonNode item) {
        // 1. API 응답 필드 추출
        String name = item.path("title").asText();
        String address = item.path("addr1").asText();
        String category = item.path("contenttypeid").asText();
        String areaCodeStr = item.path("areacode").asText();

        // 2. 위도, 경도 추출 (값이 없을 경우를 대비해 기본값 0.0 설정)
        double latitude = item.path("mapy").asDouble(0.0);
        double longitude = item.path("mapx").asDouble(0.0);

        // 3. 중복 체크 후 저장
        if (!spotRepository.existsByName(name)) {
            // 지역 코드 숫자로 변환 (전국구 대응)
            Long regionId = areaCodeStr.isEmpty() ? 1L : Long.parseLong(areaCodeStr);

            Spot spot = Spot.builder()
                    .name(name)
                    .address(address)
                    .description("전국 공공데이터 연동 정보")
                    .category(category)
                    .latitude(latitude)
                    .longitude(longitude)
                    .regionId(regionId)
                    .avgPrice(0)
                    .build();

            spotRepository.save(spot);
        }
    }
}