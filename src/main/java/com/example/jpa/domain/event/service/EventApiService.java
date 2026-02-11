package com.example.jpa.domain.event.service;

import com.example.jpa.domain.event.entity.Event;
import com.example.jpa.domain.event.repository.EventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventApiService {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${api.public-data.url}")
    private String apiUrl;

    @Value("${api.public-data.service-key}")
    private String serviceKey;

    @PostConstruct
    public void init() {
        log.info("Log4j2 - [1. 진입] @PostConstruct 실행");
        fetchAndSaveEvents();
    }

    @Transactional
    public void fetchAndSaveEvents() {
        log.info("Log4j2 - [시작] API 호출 시도");

        try {
            // 1. URL 생성 - 서비스키 중복 인코딩 방지를 위해 String으로 먼저 조합 후 URI 생성
            String urlString = apiUrl + "/searchFestival2"
                    + "?serviceKey=" + serviceKey
                    + "&_type=json"
                    + "&MobileOS=ETC"
                    + "&MobileApp=AppTest"
                    + "&numOfRows=500"
                    + "&pageNo=1"
                    + "&eventStartDate=20260210";

            URI uri = new URI(urlString);
            log.info("Log4j2 - [요청] URL 확인: {}", uri);

            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            log.info("Log4j2 - [응답] HTTP 상태 코드: {}", responseCode);

            BufferedReader rd;
            if (responseCode >= 200 && responseCode <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null)
                sb.append(line);
            rd.close();

            String responseBody = sb.toString();

            if (responseCode != 200) {
                log.error("Log4j2 - [에러] API 요청 실패. 응답 내용: {}", responseBody);
                generateMockData();
                return;
            }

            JsonNode root = objectMapper.readTree(responseBody);

            // resultCode 추출 (중첩된 경우와 상위 레벨에 있는 경우 둘 다 대응)
            JsonNode headerNode = root.path("response").path("header");
            String resultCode = headerNode.path("resultCode").isMissingNode()
                    ? root.path("resultCode").asText()
                    : headerNode.path("resultCode").asText();

            String resultMsg = headerNode.path("resultMsg").isMissingNode()
                    ? root.path("resultMsg").asText()
                    : headerNode.path("resultMsg").asText();

            JsonNode bodyNode = root.path("response").path("body");
            JsonNode items = bodyNode.path("items").path("item");

            if ("00".equals(resultCode) || "0000".equals(resultCode) || "0".equals(resultCode)) {
                if (items != null && items.isArray() && items.size() > 0) {
                    saveEventNodes(items);
                } else {
                    log.warn("Log4j2 - [주의] 성공했으나 데이터(item)가 없습니다. 응답 내용: {}", responseBody);
                    // 데이터가 하나도 없을 때만 목업을 만듭니다.
                    if (eventRepository.count() == 0)
                        generateMockData();
                }
            } else {
                log.error("Log4j2 - [주의] API 응답 에러 (코드: {}, 메시지: {}).", resultCode, resultMsg);
                log.error("Log4j2 - [필독] 서버 실제 응답: {}", responseBody);
                if (eventRepository.count() == 0)
                    generateMockData();
            }

        } catch (Exception e) {
            log.error("Log4j2 - [치명적 에러] 원인: {}", e.getMessage(), e);
            generateMockData();
        }
    }

    // 목업 데이터 생성 (API 형식을 100% 따릅니다)
    private void generateMockData() {
        log.info("Log4j2 - [Mock] 양질의 임시 데이터를 생성합니다.");

        String[][] mocks = {
                { "9001", "봄꽃 가득한 우리 동네 축제", "서울시 강남구 삼성동 1", "02-123-4567", "A0208", "20260301", "20260315" },
                { "9002", "맛있는 길거리 야시장", "서울시 관악구 신림로 2", "02-999-8888", "A0502", "20260410", "20260420" },
                { "9003", "한겨울 눈썰매 테마파크", "강원도 평창군 대관령면 3", "033-111-2222", "A0207", "20260101", "20260228" }
        };

        for (String[] data : mocks) {
            Long cId = Long.parseLong(data[0]);
            // 중복 방지 - contentId로 이미 존재하는지 필수 체크!
            if (eventRepository.findByContentId(cId).isPresent())
                continue;

            Event mock = Event.builder()
                    .contentId(cId)
                    .name(data[1])
                    .address(data[2])
                    .tel(data[3])
                    .cat2(data[4]) // 코드로 먼저 넣고
                    .imageUrl("/images/default-event.jpg")
                    .startDate(data[5])
                    .endDate(data[6])
                    .description(String.format("[%s] 행사에 참여해보세요!", data[1])) // [추가]
                    .build();

            // 키워드로 다시 한번 세밀하게 분류
            mock.setCat2(determineCategory(data[1], data[4]));

            eventRepository.save(mock);
        }
    }

    // 저장 로직 (필드 매핑 + 스마트 카테고리 분류)
    private void saveEventNodes(JsonNode items) {
        int count = 0;
        for (JsonNode node : items) {
            Long cId = node.path("contentid").asLong();
            Event event = eventRepository.findByContentId(cId).orElse(new Event());

            event.setContentId(cId);
            event.setName(node.path("title").asText());
            event.setAddress(node.path("addr1").asText());
            event.setAddr2(node.path("addr2").asText());
            event.setZipCode(node.path("zipcode").asText());
            event.setTel(node.path("tel").asText());
            event.setImageUrl(node.path("firstimage").asText());
            event.setCat1(node.path("cat1").asText());

            // [분류 스마트화] cat2 코드를 바탕으로 하되, 제목 키워드 분석을 병행함
            String apiCat2 = node.path("cat2").asText();
            event.setCat2(determineCategory(event.getName(), apiCat2));

            event.setCat3(node.path("cat3").asText());
            event.setStartDate(node.path("eventstartdate").asText());
            event.setEndDate(node.path("eventenddate").asText());
            event.setMapX(node.path("mapx").asDouble());
            event.setMapY(node.path("mapy").asDouble());

            // [설명글 자동 생성] 제목, 주소, 전화번호를 조합하여 풍성하게 만듦
            String generatedDesc = String.format("[%s] 축제는 %s에서 진행됩니다. 문의처: %s",
                    event.getName(), event.getAddress(), event.getTel().isEmpty() ? "정보 없음" : event.getTel());
            event.setDescription(generatedDesc);

            eventRepository.save(event);
            count++;
        }
        log.info("Log4j2 - [성공] {}개의 실제 API 데이터를 스마트 분류 및 설명글 생성 후 저장했습니다.", count);
    }

    /**
     * 제목 키워드와 API 코드를 조합하여 최적의 카테고리명을 반환합니다.
     */
    private String determineCategory(String title, String catCode) {
        // 1. 제목 기반 키워드 우선 분석 (catCode가 비어있거나 불분명할 때 유용)
        if (title.contains("축제") || title.contains("페스티벌") || title.contains("문화제"))
            return "축제";
        if (title.contains("야시장") || title.contains("먹거리") || title.contains("푸드") || title.contains("맛"))
            return "먹거리";
        if (title.contains("테마") || title.contains("투어") || title.contains("빛") || title.contains("겨울"))
            return "시즌테마";

        // 2. API가 준 코드 기반 분석 (A0208: 축제, A0502: 식음료 등)
        if ("A0208".equals(catCode))
            return "축제";
        if ("A0502".equals(catCode))
            return "먹거리";
        if ("A0207".equals(catCode))
            return "시즌테마";

        // 3. 분류가 어려울 경우 기본값
        return "일반행사";
    }

}