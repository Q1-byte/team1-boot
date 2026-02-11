package com.example.jpa.domain.keyword.service;

import com.example.jpa.domain.keyword.entity.Keyword;
import com.example.jpa.domain.keyword.entity.SpotKeyword;
import com.example.jpa.domain.keyword.repository.KeywordRepository;
import com.example.jpa.domain.plan.entity.TravelSpot;
import com.example.jpa.domain.plan.repository.TravelSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeywordBatchService {

    private final TravelSpotRepository travelSpotRepository;
    private final KeywordRepository keywordRepository;

    @Transactional
    public void runInitialCategorization() {
        // 1. 모든 장소 가져오기
        List<TravelSpot> allSpots = travelSpotRepository.findAll();
        System.out.println(">>> DB에서 가져온 장소 개수: " + allSpots.size());

        // 2. 모든 키워드 가져와서 맵에 저장 (ID 1~8 자동 대응)
        List<Keyword> allKeywords = keywordRepository.findAll();
        Map<Integer, Keyword> keywordMap = new HashMap<>();
        for (Keyword k : allKeywords) {
            keywordMap.put(k.getId(), k);
        }
        System.out.println(">>> 로드된 키워드 개수: " + keywordMap.size());

        int successCount = 0;

        for (TravelSpot spot : allSpots) {
            // 이미 키워드가 있으면 패스
            if (spot.getSpotKeywords() != null && !spot.getSpotKeywords().isEmpty()) {
                continue;
            }

            // 키워드 분석
            Integer targetId = assignKeywordId(spot.getName(), spot.getDescription());

            if (targetId != null && keywordMap.containsKey(targetId)) {
                Keyword targetKeyword = keywordMap.get(targetId);

                // 연결 객체 생성
                SpotKeyword connector = SpotKeyword.builder()
                        .spot(spot)
                        .keyword(targetKeyword)
                        .build();

                // 리스트에 추가
                spot.getSpotKeywords().add(connector);
                successCount++;

                // 100개마다 로그 출력 (잘 돌아가는지 확인용)
                if (successCount % 100 == 0) {
                    System.out.println(">>> 현재 " + successCount + "개 매핑 중...");
                }
            }
        }

        // 최종 결과 출력
        System.out.println("✅ 최종 매핑 완료! 총 " + successCount + "개의 장소에 키워드가 부여되었습니다.");
    }

    private Integer assignKeywordId(String name, String desc) {
        if (name == null) return null;
        String target = (name + (desc != null ? desc : "")).replaceAll("\\s", ""); // 공백 제거 후 검색

        if (target.contains("사찰") || target.contains("불교") || target.contains("사") || target.contains("명상")) return 1;
        if (target.contains("공원") || target.contains("숲") || target.contains("수목원") || target.contains("자연")) return 2;
        if (target.contains("길") || target.contains("트래킹") || target.contains("산행") || target.contains("코스")) return 3;
        if (target.contains("로데오") || target.contains("가로수길") || target.contains("쇼핑") || target.contains("거리"))
            return 4;
        if (target.contains("VR") || target.contains("액티비티") || target.contains("테마파크") || target.contains("랜드"))
            return 5;
        if (target.contains("역사") || target.contains("박물관") || target.contains("기념관") || target.contains("유적"))
            return 6;
        if (target.contains("갤러리") || target.contains("미술관") || target.contains("전시") || target.contains("아트"))
            return 7;
        if (target.contains("체험") || target.contains("공방") || target.contains("클래스") || target.contains("만들기"))
            return 8;

        return null; //추가
    }
}