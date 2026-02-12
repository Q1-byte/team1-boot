package com.example.jpa.domain.gacha.service;

import com.example.jpa.domain.gacha.dto.GachaResponseDto;
import com.example.jpa.domain.keyword.entity.Keyword; // 키워드 엔티티 경로 확인 필요
import com.example.jpa.domain.keyword.repository.KeywordRepository; // 키워드 레포지토리 경로 확인 필요
import com.example.jpa.domain.spot.entity.Spot;
import com.example.jpa.domain.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 아래 4가지 임포트가 핵심입니다!
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GachaService {

    private final SpotRepository spotRepository;
    private final KeywordRepository keywordRepository; // 1. 주입 추가

    public GachaResponseDto drawGacha(Integer level) {
        // 1. 랜덤 장소 뽑기
        Spot spot = spotRepository.findRandomSpotByLevel(level)
                .orElseThrow(() -> new RuntimeException("해당 레벨의 장소가 없습니다."));

        // 2. 지역 정보 안전하게 추출 (Null 체크)
        // spot.getRegion()이 null인 '지뢰 데이터'를 대비합니다.
        Long regionId = (spot.getRegion() != null) ? spot.getRegion().getId() : null;
        String regionName = (spot.getRegion() != null) ? spot.getRegion().getName() : "지역 정보 없음";

        // 3. "내가 가진 키워드 풀에서 랜덤 3개" 로직
        List<Keyword> allKeywords = keywordRepository.findAll();

        // 이름(String)만 추출해서 리스트화
        List<String> keywordNames = allKeywords.stream()
                .map(Keyword::getName)
                .collect(Collectors.toList());

        // 무작위로 섞기
        Collections.shuffle(keywordNames);

        // 섞인 것 중 상위 3개만 추출 (데이터가 3개 미만일 경우를 대비해 처리)
        List<String> randomKeywords = keywordNames.stream()
                .limit(3)
                .collect(Collectors.toList());

        // 4. DTO 생성 및 반환
        // GachaService.java 수정 예시
        return GachaResponseDto.builder()
                .id(spot.getId())
                // .name(spot.getName()) // 기존: 육삼월 충주본점
                .name(spot.getRegion().getName()) // 변경: 충청북도 (Region의 이름 사용)
                .regionId(spot.getRegion().getId())
                .regionName(spot.getRegion().getName())
                .keywords(randomKeywords)
                .build();
    }
}