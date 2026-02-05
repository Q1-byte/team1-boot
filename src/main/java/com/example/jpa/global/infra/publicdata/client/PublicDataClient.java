package com.example.jpa.global.infra.publicdata.client;

import com.example.jpa.global.infra.publicdata.dto.PublicDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class PublicDataClient {

    private final RestTemplate restTemplate;

    // application.yml에 정의된 값을 가져옵니다.
    @Value("${api.public-data.service-key}")
    private String serviceKey;

    @Value("${api.public-data.url}")
    private String baseUrl;

    /**
     * 공공데이터 API를 호출하여 응답을 받아옵니다.
     * @param categoryCode 조회할 카테고리 코드 (1Depth 등)
     * @return 파싱된 JSON 응답 객체
     */
    public PublicDataResponse fetchApiData(String categoryCode) {

        // 인증키에 %, = 등이 포함된 경우 build(true)를 사용해 인코딩 깨짐을 방지합니다.
        URI uri = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("_type", "json")
                .queryParam("cat1", categoryCode)
                .build(true)
                .toUri();

        return restTemplate.getForObject(uri, PublicDataResponse.class);
    }
}