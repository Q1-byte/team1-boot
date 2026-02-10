package com.example.jpa.service;

import com.example.jpa.domain.spot.service.SpotService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    private final SpotService spotService;
    private final RestTemplate restTemplate;

    @Value("${api.public-data.service-key}")
    private String serviceKey;

    public DataService(SpotService spotService) {
        this.spotService = spotService;
        this.restTemplate = new RestTemplate();
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        this.restTemplate.setUriTemplateHandler(factory);
    }

    // 지역 정보를 담을 간단한 내부 클래스
    private static class TargetRegion {
        String name;
        String areaCode;
        String sigunguCode;

        TargetRegion(String name, String areaCode, String sigunguCode) {
            this.name = name;
            this.areaCode = areaCode;
            this.sigunguCode = sigunguCode;
        }
    }

    public void fetchAndDistributeData() {
         // 2. 요청하신 9개 지역 설정
        List<TargetRegion> targets = new ArrayList<>();
        targets.add(new TargetRegion("서울 종로구", "1", "23"));
        targets.add(new TargetRegion("경기 용인시", "31", "17"));
        targets.add(new TargetRegion("강원 속초시", "32", "5"));
        targets.add(new TargetRegion("충북 단양군", "33", "2"));
        targets.add(new TargetRegion("충남 부여군", "34", "6"));
        targets.add(new TargetRegion("전북 전주시", "35", "11"));
        targets.add(new TargetRegion("전남 여수시", "38", "13"));
        targets.add(new TargetRegion("경북 경주시", "35", "2")); // API 기준 경북은 35(지역에 따라 다를 수 있음)
        targets.add(new TargetRegion("부산 전체", "6", ""));    // 부산시는 광역시(Code 6) 전체로 설정

        int numOfRows = 50; // 한 페이지에 50개씩
        int maxPages = 10;  // 50개 * 10페이지 = 지역당 500개

        for (TargetRegion region : targets) {
            System.out.println("📍 [" + region.name + "] 수집 시작 (목표: 500개)");

            for (int pageNo = 1; pageNo <= maxPages; pageNo++) {
                UriComponentsBuilder builder = UriComponentsBuilder
                        .fromHttpUrl("https://apis.data.go.kr/B551011/KorService2/areaBasedList2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("numOfRows", numOfRows)
                        .queryParam("pageNo", pageNo)
                        .queryParam("areaCode", region.areaCode)
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "AppTest")
                        .queryParam("_type", "json")
                        .queryParam("arrange", "A")
                        .queryParam("firstImageYN", "Y");;

                // 시군구 코드가 있는 경우만 파라미터 추가
                if (!region.sigunguCode.isEmpty()) {
                    builder.queryParam("sigunguCode", region.sigunguCode);
                }

                URI uri = builder.build(true).toUri();

                try {
                    String rawResponse = restTemplate.getForObject(uri, String.class);
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(rawResponse);
                    JsonNode items = root.path("response").path("body").path("items").path("item");

                    if (items.isArray() && !items.isEmpty()) {
                        for (JsonNode item : items) {
                            spotService.registerSpot(
                                    item.path("title").asText(),
                                    item.path("addr1").asText(),
                                    item.path("mapx").asText(),
                                    item.path("mapy").asText()
                            );
                        }
                        System.out.println("   - " + pageNo + "페이지 완료 (" + (pageNo * numOfRows) + "개)");
                    } else {
                        break; // 더 이상 데이터 없으면 다음 지역으로
                    }
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.err.println("❌ 에러: " + e.getMessage());
                }
            }
        }
        System.out.println("🏁 모든 지역 수집 완료!");
    }
}