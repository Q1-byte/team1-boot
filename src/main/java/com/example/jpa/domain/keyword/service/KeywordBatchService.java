package com.example.jpa.domain.keyword.service;

import com.example.jpa.domain.accommodation.entity.Accommodation;
import com.example.jpa.domain.accommodation.repository.AccommodationRepository;
import com.example.jpa.domain.activity.entity.Activity;
import com.example.jpa.domain.activity.repository.ActivityRepository;
import com.example.jpa.domain.keyword.entity.Keyword;
import com.example.jpa.domain.keyword.entity.SpotKeyword;
import com.example.jpa.domain.keyword.repository.KeywordRepository;
import com.example.jpa.domain.plan.entity.TravelSpot;
import com.example.jpa.domain.plan.repository.TravelSpotRepository;
import com.example.jpa.domain.ticket.entity.Ticket;
import com.example.jpa.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KeywordBatchService {

    private final TravelSpotRepository travelSpotRepository;
    private final AccommodationRepository accommodationRepository;
    private final ActivityRepository activityRepository;
    private final TicketRepository ticketRepository;
    private final KeywordRepository keywordRepository;

    // TravelSpot에서 제외할 단어 (숙소 성격 장소 필터링)
    private static final List<String> EXCLUDED_WORDS = List.of("캠핑", "리조트", "야영", "글램핑", "카라반", "오토캠핑");

    // 키워드별 매칭 패턴 정의
    private static final Map<String, List<String>> KEYWORD_PATTERNS = new LinkedHashMap<>();

    static {
        // [테마] 카테고리
        KEYWORD_PATTERNS.put("스릴", List.of("놀이기구", "번지점프", "래프팅", "스카이다이빙", "짚라인", "스릴", "롤러코스터", "바이킹", "카트", "VR", "어드벤처", "서바이벌", "ATV", "패러글라이딩", "보트"));
        KEYWORD_PATTERNS.put("자연", List.of("공원", "숲", "수목원", "자연", "생태", "산림", "식물원", "습지", "계곡", "폭포", "해변", "바다", "강", "호수", "섬", "산", "들", "정원", "녹지", "하늘", "일출", "일몰"));
        KEYWORD_PATTERNS.put("힐링", List.of("스파", "온천", "찜질", "힐링", "명상", "요가", "테라피", "휴양", "사우나", "족욕", "마사지", "웰니스", "쉼", "휴식", "산책", "조용", "평화", "여유"));
        KEYWORD_PATTERNS.put("트레킹", List.of("등산", "트레킹", "산행", "하이킹", "둘레길", "올레길", "산책로", "탐방로", "코스", "길", "봉", "재", "고개", "능선", "숲길", "해파랑길"));
        KEYWORD_PATTERNS.put("데이트", List.of("카페", "레스토랑", "데이트", "야경", "전망대", "루프탑", "커플", "분위기", "맛집", "디저트", "베이커리", "와인", "브런치", "파스타", "이탈리안", "프렌치"));
        KEYWORD_PATTERNS.put("추억", List.of("사진", "포토", "추억", "인생샷", "스냅", "촬영", "포토존", "벽화", "마을", "거리", "야시장", "시장", "먹거리", "축제"));
        KEYWORD_PATTERNS.put("예술", List.of("미술관", "갤러리", "전시", "아트", "예술", "공연", "뮤지컬", "연극", "콘서트", "박물관", "기념관", "문화", "역사", "유적", "고궁", "사찰", "서원"));
        KEYWORD_PATTERNS.put("체험", List.of("체험", "공방", "클래스", "만들기", "DIY", "쿠킹", "도예", "염색", "목공", "농장", "목장", "낚시", "수확", "떡", "한복", "전통", "공예"));

        // [환경] 카테고리
        KEYWORD_PATTERNS.put("오션뷰", List.of("바다", "해변", "오션", "해수욕", "해안", "갯벌", "항구", "포구", "등대"));
        KEYWORD_PATTERNS.put("역세권", List.of("역세권", "역 근처", "지하철", "KTX", "SRT"));
        KEYWORD_PATTERNS.put("시티뷰", List.of("시티뷰", "도시", "도심", "빌딩", "스카이라인"));
        KEYWORD_PATTERNS.put("숲세권", List.of("숲세권", "산림욕", "자연휴양림", "숲속", "삼림"));
        KEYWORD_PATTERNS.put("호수뷰", List.of("호수", "저수지", "강변", "하천", "수변", "호반"));
        KEYWORD_PATTERNS.put("전통/역사", List.of("한옥", "전통", "역사", "문화재", "유적", "고궁", "사찰", "성곽", "서원", "박물관", "기념관"));
        KEYWORD_PATTERNS.put("시내중심", List.of("시내", "중심가", "번화가", "다운타운", "상권", "쇼핑"));

        // [조건] 카테고리
        KEYWORD_PATTERNS.put("가족친화", List.of("가족", "어린이", "키즈", "아이", "유아", "패밀리", "놀이터"));
        KEYWORD_PATTERNS.put("가성비", List.of("가성비", "저렴", "합리적", "무료", "할인"));
        KEYWORD_PATTERNS.put("조용한", List.of("조용", "한적", "프라이빗", "독채", "단독", "고요"));
        KEYWORD_PATTERNS.put("반려동물동반", List.of("반려", "애견", "펫", "동물동반", "강아지"));
        KEYWORD_PATTERNS.put("비즈니스", List.of("비즈니스", "회의", "세미나", "컨퍼런스", "워크숍"));
        KEYWORD_PATTERNS.put("럭셔리", List.of("럭셔리", "프리미엄", "특급", "5성", "스위트", "풀빌라"));
        KEYWORD_PATTERNS.put("루프탑/야외", List.of("루프탑", "야외", "테라스", "옥상", "가든", "정원", "바베큐"));
        KEYWORD_PATTERNS.put("취사가능", List.of("취사", "주방", "키친", "조리", "셀프쿠킹"));
    }

    @Transactional
    public void runInitialCategorization() {
        // 이미 키워드 매핑이 되어 있으면 스킵
        long spotKeywordCount = keywordRepository.countMappedSpotKeywords();
        if (spotKeywordCount > 0) {
            System.out.println(">>> 키워드 매핑 데이터가 이미 존재합니다 (" + spotKeywordCount + "건). 배치를 건너뜁니다.");
            return;
        }

        // 1. DB에서 모든 키워드 로드 (name 기준 맵)
        List<Keyword> allKeywords = keywordRepository.findAll();
        Map<String, Keyword> keywordByName = new HashMap<>();
        for (Keyword k : allKeywords) {
            keywordByName.put(k.getName(), k);
        }
        System.out.println(">>> 로드된 키워드 개수: " + keywordByName.size());

        // 2. TravelSpot 키워드 매핑
        categorizeTravelSpots(keywordByName);

        // 3. Accommodation 키워드 매핑
        categorizeAccommodations(keywordByName);

        // 4. Activity 키워드 매핑
        categorizeActivities(keywordByName);

        // 5. Ticket 키워드 매핑
        categorizeTickets(keywordByName);
    }

    private void categorizeTravelSpots(Map<String, Keyword> keywordByName) {
        List<TravelSpot> allSpots = travelSpotRepository.findAll();
        System.out.println(">>> TravelSpot 개수: " + allSpots.size());

        int successCount = 0;
        int skipCount = 0;
        for (TravelSpot spot : allSpots) {
            if (spot.getSpotKeywords() != null && !spot.getSpotKeywords().isEmpty()) {
                continue;
            }

            // 숙소 성격 장소 제외
            if (isExcludedSpot(spot.getName(), spot.getDescription())) {
                skipCount++;
                continue;
            }

            List<String> matched = matchKeywords(spot.getName(), spot.getDescription());
            for (String keywordName : matched) {
                Keyword keyword = keywordByName.get(keywordName);
                if (keyword != null) {
                    SpotKeyword connector = SpotKeyword.builder()
                            .spot(spot)
                            .keyword(keyword)
                            .build();
                    spot.getSpotKeywords().add(connector);
                }
            }

            if (!matched.isEmpty()) {
                successCount++;
                if (successCount % 100 == 0) {
                    System.out.println(">>> TravelSpot " + successCount + "개 매핑 중...");
                }
            }
        }
        System.out.println(">>> TravelSpot 매핑 완료: " + successCount + "개 (제외: " + skipCount + "개)");
    }

    private boolean isExcludedSpot(String name, String description) {
        String target = (name != null ? name : "") + (description != null ? description : "");
        for (String word : EXCLUDED_WORDS) {
            if (target.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private void categorizeAccommodations(Map<String, Keyword> keywordByName) {
        List<Accommodation> all = accommodationRepository.findAll();
        System.out.println(">>> Accommodation 개수: " + all.size());

        int successCount = 0;
        for (Accommodation acc : all) {
            if (acc.getKeywords() != null && !acc.getKeywords().isEmpty()) {
                continue;
            }

            List<String> matched = matchKeywords(acc.getName(), acc.getDescription());
            if (!matched.isEmpty()) {
                acc.setKeywords(String.join(",", matched));
                successCount++;
                if (successCount % 100 == 0) {
                    System.out.println(">>> Accommodation " + successCount + "개 매핑 중...");
                }
            }
        }
        accommodationRepository.saveAll(all);
        System.out.println(">>> Accommodation 매핑 완료: " + successCount + "개");
    }

    private void categorizeActivities(Map<String, Keyword> keywordByName) {
        List<Activity> all = activityRepository.findAll();
        System.out.println(">>> Activity 개수: " + all.size());

        int successCount = 0;
        for (Activity activity : all) {
            if (activity.getKeywords() != null && !activity.getKeywords().isEmpty()) {
                continue;
            }

            List<String> matched = matchKeywords(activity.getName(), activity.getDescription());
            if (!matched.isEmpty()) {
                activity.setKeywords(String.join(",", matched));
                successCount++;
                if (successCount % 100 == 0) {
                    System.out.println(">>> Activity " + successCount + "개 매핑 중...");
                }
            }
        }
        activityRepository.saveAll(all);
        System.out.println(">>> Activity 매핑 완료: " + successCount + "개");
    }

    private void categorizeTickets(Map<String, Keyword> keywordByName) {
        List<Ticket> all = ticketRepository.findAll();
        System.out.println(">>> Ticket 개수: " + all.size());

        int successCount = 0;
        for (Ticket ticket : all) {
            if (ticket.getKeywords() != null && !ticket.getKeywords().isEmpty()) {
                continue;
            }

            List<String> matched = matchKeywords(ticket.getName(), ticket.getDescription());
            if (!matched.isEmpty()) {
                ticket.setKeywords(String.join(",", matched));
                successCount++;
                if (successCount % 100 == 0) {
                    System.out.println(">>> Ticket " + successCount + "개 매핑 중...");
                }
            }
        }
        ticketRepository.saveAll(all);
        System.out.println(">>> Ticket 매핑 완료: " + successCount + "개");
    }

    /**
     * 이름과 설명을 분석하여 매칭되는 키워드 이름 리스트를 반환
     */
    private List<String> matchKeywords(String name, String description) {
        if (name == null) return Collections.emptyList();
        String target = (name + (description != null ? description : "")).replaceAll("\\s", "");

        List<String> matched = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : KEYWORD_PATTERNS.entrySet()) {
            String keywordName = entry.getKey();
            List<String> patterns = entry.getValue();

            for (String pattern : patterns) {
                if (target.contains(pattern)) {
                    matched.add(keywordName);
                    break; // 하나라도 매칭되면 해당 키워드 추가 후 다음 키워드로
                }
            }
        }
        return matched;
    }
}
