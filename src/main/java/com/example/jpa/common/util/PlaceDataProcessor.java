package com.example.jpa.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceDataProcessor {

    public static void main(String[] args) {
        // 1. 데이터 파일 경로 (본인의 실제 파일 경로로 수정하세요)
        String filePath = "C:/YOUR_PATH/data.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("-- SQL INSERT 문 생성을 시작합니다.");

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                processRawData(line);
            }

            System.out.println("-- 모든 데이터 처리가 완료되었습니다.");
        } catch (IOException e) {
            System.err.println("파일을 읽는 중 에러 발생: " + e.getMessage());
        }
    }

    public static void processRawData(String rawLine) {
        // 정규표현식: [ID][제목][숫자][주소][위도][경도] 순서 파싱
        Pattern pattern = Pattern.compile("(\\d{6,})([가-힣\\s\\(\\)\\[\\]\\&]+)(\\d)(서울특별시[가-힣\\s\\d\\(\\),-]+)(\\d+\\.\\d+)(\\d+\\.\\d+)");
        Matcher matcher = pattern.matcher(rawLine);

        if (matcher.find()) {
            String spotId = matcher.group(1);
            String title = matcher.group(2).trim();

            // 중복 방지를 위한 Set 사용 후 List 변환
            Set<String> keywordSet = new LinkedHashSet<>();

            // 1. #자연 (산, 숲, 공원, 유수지 등)
            if (title.matches(".*(공원|산|숲|길|정릉|유수지|해맞이|동산|천|강|호수).*")) {
                keywordSet.add("#자연");
            }

            // 2. #트래킹 (산, 둘레길, 등산로 등)
            if (title.matches(".*(산|둘레길|등산|코스|나들길).*")) {
                keywordSet.add("#트래킹");
            }

            // 3. #예술 (미술관, 갤러리, 아트, 뮤지엄, 전시관 등)
            if (title.matches(".*(미술관|갤러리|아트|뮤지엄|전시|예술|홀|문화|공연).*")) {
                keywordSet.add("#예술");
            }

            // 4. #체험 (박물관, 센터, 스튜디오, 아쿠아리움, 체험관 등)
            if (title.matches(".*(박물관|센터|스튜디오|체험|아쿠아리움|도서관|과학관|교육).*")) {
                keywordSet.add("#체험");
            }

            // 5. #데이트 (거리, 로데오, 가로수길, 스토어, 카페, 베이커리 등)
            if (title.matches(".*(거리|로데오|가로수길|스토어|카페|베이커리|맛집|쇼핑|몰).*")) {
                keywordSet.add("#데이트");
            }

            // 6. #스릴 (놀이공원, 스포츠, 레저, 테마파크, 게임 등)
            if (title.matches(".*(월드|랜드|레저|스포츠|경기장|스타디움|엑스포).*")) {
                keywordSet.add("#스릴");
            }

            // 7. #힐링 (절, 사원, 명상, 온천, 스파, 수목원 등)
            // 자연이나 예술이 포함된 곳도 보통 힐링에 해당하므로 추가 로직 적용
            if (title.matches(".*(사|선원|묘|공원|수목원|식물원|온천|스파|서점|책방).*") || keywordSet.contains("#자연")) {
                keywordSet.add("#힐링");
            }

            // 8. #추억 (역사적 장소나 모든 장소의 기본값)
            // 모든 장소에는 기본적으로 추억을 쌓을 수 있으므로 무조건 추가
            keywordSet.add("#추억");

            // 최종 생성된 키워드가 하나도 없을 경우(필터에 안 걸릴 경우) 대비
            if (keywordSet.size() <= 1) { // #추억만 있는 경우
                keywordSet.add("#힐링"); // 기본값으로 힐링 부여
            }

            generateInsertSql(spotId, title, new ArrayList<>(keywordSet));
        }
    }

    private static void generateInsertSql(String spotId, String title, List<String> keywords) {
        System.out.println("-- 장소: " + title);
        for (String kw : keywords) {
            System.out.printf(
                    "INSERT INTO spot_keyword (spot_id, keyword_id) VALUES (%s, (SELECT id FROM keyword WHERE name = '%s'));\n",
                    spotId, kw
            );
        }
        System.out.println();
    }
}