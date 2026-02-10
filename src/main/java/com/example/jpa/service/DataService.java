package com.example.jpa.service;

import com.example.jpa.domain.spot.service.SpotService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class DataService {

    private final SpotService spotService;
    private final RestTemplate restTemplate;

    @Value("${api.public-data.service-key}")
    private String serviceKey;

    public DataService(SpotService spotService) {
        this.spotService = spotService;

        // RestTemplate 설정
        this.restTemplate = new RestTemplate();
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        this.restTemplate.setUriTemplateHandler(factory);
    }

    private record TargetRegion(String name, String areaCode, String sigunguCode) {}

    @Async
    public void fetchAndDistributeData() {
        log.info("========== 데이터 수집 시작 ==========");
        int totalSaved = 0;

        List<TargetRegion> targets = new ArrayList<>();

        // 프론트엔드 맞춤 26개+ 지역 리스트
        targets.add(new TargetRegion("서울 강남구", "1", "1"));
        targets.add(new TargetRegion("서울 종로구", "1", "23"));
        targets.add(new TargetRegion("서울 마포구", "1", "13"));
        targets.add(new TargetRegion("서울 용산구", "1", "21"));
        targets.add(new TargetRegion("경기 수원시", "31", "1"));
        targets.add(new TargetRegion("경기 용인시", "31", "17"));
        targets.add(new TargetRegion("경기 성남시", "31", "13"));
        targets.add(new TargetRegion("강원 춘천시", "32", "1"));
        targets.add(new TargetRegion("강원 강릉시", "32", "5"));
        targets.add(new TargetRegion("충북 청주시", "33", "7"));
        targets.add(new TargetRegion("충북 충주시", "33", "11"));
        targets.add(new TargetRegion("충남 천안시", "34", "8"));
        targets.add(new TargetRegion("충남 아산시", "34", "4"));
        targets.add(new TargetRegion("전북 전주시", "37", "11"));
        targets.add(new TargetRegion("전북 군산시", "37", "2"));
        targets.add(new TargetRegion("전남 여수시", "38", "13"));
        targets.add(new TargetRegion("전남 순천시", "38", "11"));
        targets.add(new TargetRegion("경북 포항시", "35", "23"));
        targets.add(new TargetRegion("경북 경주시", "35", "2"));
        targets.add(new TargetRegion("경남 창원시", "36", "16"));
        targets.add(new TargetRegion("경남 진주시", "36", "13"));
        targets.add(new TargetRegion("부산 해운대구", "6", "9"));
        targets.add(new TargetRegion("부산 기장군", "6", "16"));
        targets.add(new TargetRegion("제주 제주시", "39", "1"));
        targets.add(new TargetRegion("제주 서귀포시", "39", "2"));
        targets.add(new TargetRegion("인천 중구", "2", "1"));
        targets.add(new TargetRegion("인천 남동구", "2", "4"));
        targets.add(new TargetRegion("울산 남구", "7", "2"));
        targets.add(new TargetRegion("울산 북구", "7", "3"));

        // 수집 대상 카테고리 (숙소 제외)
        List<String> contentTypes = List.of("12", "14", "28", "39");

        for (TargetRegion region : targets) {
            for (String contentTypeId : contentTypes) {
                // 상향 조정된 수집량 (75개)
                UriComponentsBuilder builder = UriComponentsBuilder
                        .fromHttpUrl("https://apis.data.go.kr/B551011/KorService2/areaBasedList2")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("numOfRows", 75)
                        .queryParam("pageNo", 1)
                        .queryParam("areaCode", region.areaCode)
                        .queryParam("contentTypeId", contentTypeId)
                        .queryParam("MobileOS", "ETC")
                        .queryParam("MobileApp", "AppTest")
                        .queryParam("_type", "json")
                        .queryParam("arrange", "A");

                // 시군구 코드가 있는 경우에만 추가
                if (!region.sigunguCode.isEmpty()) {
                    builder.queryParam("sigunguCode", region.sigunguCode);
                }

                URI uri = builder.build(true).toUri();

                try {
                    String rawResponse = restTemplate.getForObject(uri, String.class);
                    log.info("API call done - area={} sigungu={} cat={} responseLen={} body={}",
                            region.areaCode, region.sigunguCode, contentTypeId,
                            rawResponse != null ? rawResponse.length() : 0,
                            rawResponse != null ? rawResponse.substring(0, Math.min(300, rawResponse.length())) : "null");

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(rawResponse);
                    JsonNode items = root.path("response").path("body").path("items").path("item");

                    if (items.isArray() && !items.isEmpty()) {
                        int count = 0;
                        for (JsonNode item : items) {
                            spotService.registerSpot(
                                    item.path("contentid").asText(),
                                    item.path("title").asText(),
                                    item.path("addr1").asText(),
                                    item.path("mapx").asText(),
                                    item.path("mapy").asText(),
                                    item.path("firstimage").asText(),
                                    item.path("contenttypeid").asText(),
                                    item.path("areacode").asText()
                            );
                            count++;
                        }
                        totalSaved += count;
                        log.info("SAVED - area={} cat={} count={}", region.areaCode, contentTypeId, count);
                    } else {
                        log.warn("EMPTY - area={} sigungu={} cat={} items.isArray={} resultCode={}",
                                region.areaCode, region.sigunguCode, contentTypeId, items.isArray(),
                                root.path("response").path("header").path("resultCode").asText());
                    }
                    Thread.sleep(80);
                } catch (Exception e) {
                    log.error("ERROR - area={} cat={} : {}", region.areaCode, contentTypeId, e.getMessage());
                }
            }
        }
        log.info("========== 수집 완료! 총 {}건 처리 ==========", totalSaved);
    }
}