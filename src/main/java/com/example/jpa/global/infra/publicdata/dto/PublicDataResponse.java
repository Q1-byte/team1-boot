package com.example.jpa.global.infra.publicdata.dto; // 1. 패키지 경로 재확인

import lombok.Data;

import java.util.List;

@Data // 2. 여기서 빨간색이 뜨면 Alt+Enter로 Import 추가
public class PublicDataResponse {
    private Response response;

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }

    @Data
    public static class Body {
        private Items items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    public static class Items {
        private List<PublicItemDto> item;
    }

    @Data
    public static class PublicItemDto {
        // 공공데이터 명세서에 있는 필드들을 여기에 추가
        private String code;
        private String name;
        // 추가로 필요한 필드들 (예: addr1, title 등)을 여기에 선언하세요.
    }
}