package com.example.jpa.domain.gacha.dto;

import com.example.jpa.domain.spot.entity.Spot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GachaResponseDto {
    private Long id;              // Spot ID
    private String name;
    private Long regionId;// 관광지명
    private String regionName;    // 지역명 (cityName)
    private String desc;          // 설명 (description)
    private List<String> keywords; // 가공된 키워드 리스트
    private String areaCode;
    private String sigunguCode;

    public GachaResponseDto(Spot spot) {
        this.id = spot.getId();
        this.name = spot.getName();

        // 📍 Region 엔티티의 cityName 필드 매핑
        if (spot.getRegion() != null) {
            this.regionName = spot.getRegion().getName();
            this.areaCode = spot.getRegion().getAreaCode();
            this.sigunguCode = spot.getRegion().getSigunguCode();
        }

        // 📍 프론트엔드 변수명 desc에 description 매핑
        this.desc = spot.getDescription();

        // 📍 엔티티의 가공 메서드 호출
        this.keywords = spot.getKeywordsList();
    }
}