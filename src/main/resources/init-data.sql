-- ============================================================
-- 통합 초기화 SQL (init-data.sql)
-- 팀원 누구나 이 파일 한번 실행하면 목업 데이터 세팅 완료
--
-- 사전 조건: region 테이블에 데이터가 있어야 합니다 (서버 최초 기동 시 자동 생성)
-- region ID 기준: 1=서울, 2=인천, 3=부산, 4=울산, 5=경기, 6=강원,
--                 7=충북, 8=충남, 9=경북, 10=경남, 11=전북, 12=전남, 13=제주
--
-- 실행 순서: 1.초기화 → 2.키워드 → 3.숙소 → 4.액티비티 → 5.티켓 → 6.이벤트 → 7.travel_spot level
-- ============================================================


-- ============================================================
-- 0. 기존 데이터 초기화
-- ============================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE spot_keyword;
TRUNCATE TABLE keyword;
TRUNCATE TABLE accommodation;
TRUNCATE TABLE activity;
TRUNCATE TABLE ticket;
SET FOREIGN_KEY_CHECKS = 1;

-- event는 TRUNCATE 대신 DELETE (content_id 기반 중복방지 되어있으므로)
DELETE FROM event WHERE content_id >= 10001;


-- ============================================================
-- 1. KEYWORD (23건)
-- ============================================================

-- [테마] 카테고리
INSERT INTO keyword (name, category, active) VALUES
('스릴', '테마', TRUE),
('자연', '테마', TRUE),
('힐링', '테마', TRUE),
('트레킹', '테마', TRUE),
('데이트', '테마', TRUE),
('추억', '테마', TRUE),
('예술', '테마', TRUE),
('체험', '테마', TRUE);

-- [환경] 카테고리
INSERT INTO keyword (name, category, active) VALUES
('오션뷰', '환경', TRUE),
('역세권', '환경', TRUE),
('시티뷰', '환경', TRUE),
('숲세권', '환경', TRUE),
('호수뷰', '환경', TRUE),
('전통/역사', '환경', TRUE),
('시내중심', '환경', TRUE);

-- [조건] 카테고리
INSERT INTO keyword (name, category, active) VALUES
('가족친화', '조건', TRUE),
('가성비', '조건', TRUE),
('조용한', '조건', TRUE),
('반려동물동반', '조건', TRUE),
('비즈니스', '조건', TRUE),
('럭셔리', '조건', TRUE),
('루프탑/야외', '조건', TRUE),
('취사가능', '조건', TRUE);


-- ============================================================
-- 2. ACCOMMODATION (숙소) - 47건
-- ============================================================

-- 서울 (region_id = 1)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('호텔 라온 강남', '강남역 도보 3분, 비즈니스 & 관광 최적 위치', 'https://loremflickr.com/400/300/gangnam,hotel?lock=1', 1, 37.5012, 127.0396, 150000, 'HOTEL', '역세권,비즈니스,데이트', true),
('르메르디앙 서울', '럭셔리 5성급 호텔, 조식 뷔페 포함', 'https://loremflickr.com/400/300/le+meridien+seoul?lock=2', 1, 37.5074, 127.0550, 280000, 'HOTEL', '럭셔리,시티뷰,추억', true),
('홍대 플레이 하우스텔', '홍대입구역 1분, 젊은 감성 숙소', 'https://loremflickr.com/400/300/hongdae,guesthouse?lock=3', 1, 37.5563, 126.9236, 45000, 'GUESTHOUSE', '가성비,역세권,데이트,예술', true),
('남산 한옥스테이', '전통 한옥 체험, 남산타워 도보권', 'https://loremflickr.com/400/300/namsan,hanok?lock=4', 1, 37.5511, 126.9883, 85000, 'GUESTHOUSE', '전통/역사,조용한,체험,힐링', true),
('이태원 부티크 호텔', '이국적 분위기의 루프탑 바 보유', 'https://loremflickr.com/400/300/itaewon,boutique+hotel?lock=5', 1, 37.5340, 126.9948, 120000, 'HOTEL', '루프탑/야외,데이트,예술', true);

-- 부산 (region_id = 3)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('파라다이스호텔 부산', '해운대 해변 바로 앞 5성급 리조트', 'https://loremflickr.com/400/300/paradise+hotel+busan?lock=6', 3, 35.1587, 129.1605, 250000, 'RESORT', '오션뷰,럭셔리,데이트', true),
('해운대 블루오션 펜션', '해운대 해변 오션뷰 가족 펜션', 'https://loremflickr.com/400/300/haeundae,ocean+view?lock=7', 3, 35.1589, 129.1600, 95000, 'PENSION', '오션뷰,가족친화,추억,자연', true),
('광안리 서프 게스트하우스', '광안리 해변 도보 2분, 서퍼 친화', 'https://loremflickr.com/400/300/gwangalli,beach?lock=8', 3, 35.1531, 129.1186, 40000, 'GUESTHOUSE', '오션뷰,가성비,체험,스릴', true),
('센텀시티 비즈니스호텔', '센텀시티 중심, 깔끔한 비즈니스호텔', 'https://loremflickr.com/400/300/centum+city,hotel?lock=9', 3, 35.1690, 129.1318, 110000, 'HOTEL', '비즈니스,역세권,시내중심,추억', true);

-- 인천 (region_id = 2)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('그랜드 하얏트 인천', '송도 국제도시 내 럭셔리 호텔', 'https://loremflickr.com/400/300/grand+hyatt+incheon?lock=10', 2, 37.3872, 126.6633, 220000, 'HOTEL', '럭셔리,비즈니스,데이트', true),
('월미도 씨사이드 펜션', '월미도 바다 전망 가족 펜션', 'https://loremflickr.com/400/300/wolmido,seaside?lock=11', 2, 37.4753, 126.5977, 80000, 'PENSION', '오션뷰,가족친화,추억', true),
('차이나타운 게스트하우스', '인천 차이나타운 중심 가성비 숙소', 'https://loremflickr.com/400/300/incheon+chinatown?lock=12', 2, 37.4740, 126.6178, 35000, 'GUESTHOUSE', '가성비,전통/역사,체험', true);

-- 울산 (region_id = 4)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('롯데호텔 울산', '울산 시내 중심 비즈니스 호텔', 'https://loremflickr.com/400/300/lotte+hotel+ulsan?lock=13', 4, 35.5389, 129.3114, 160000, 'HOTEL', '비즈니스,시내중심,데이트', true),
('간절곶 오션뷰 펜션', '한국 최동단 간절곶 일출 명소', 'https://loremflickr.com/400/300/ganjeolgot,sunrise+ocean?lock=14', 4, 35.3616, 129.3604, 90000, 'PENSION', '오션뷰,조용한,자연,힐링', true),
('태화강 게스트하우스', '태화강 국가정원 도보 5분', 'https://loremflickr.com/400/300/taehwa+river,guesthouse?lock=15', 4, 35.5485, 129.3068, 40000, 'GUESTHOUSE', '자연,가성비,힐링,트레킹', true);

-- 경기 (region_id = 5)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('수원 노보텔 앰배서더', '수원화성 인접 비즈니스 호텔', 'https://loremflickr.com/400/300/novotel+suwon?lock=16', 5, 37.2636, 127.0286, 170000, 'HOTEL', '비즈니스,역세권,전통/역사,추억', true),
('용인 에버랜드 리조트', '에버랜드 단지 내 가족 리조트', 'https://loremflickr.com/400/300/everland+resort?lock=17', 5, 37.2942, 127.2022, 200000, 'RESORT', '가족친화,체험,스릴', true),
('광교 레이크 펜션', '광교호수공원 옆 조용한 펜션', 'https://loremflickr.com/400/300/gwanggyo,lake+pension?lock=18', 5, 37.2920, 127.0455, 75000, 'PENSION', '호수뷰,조용한,자연,힐링', true),
('판교 비즈니스호텔', '판교 테크노밸리 중심 숙소', 'https://loremflickr.com/400/300/pangyo,business+hotel?lock=19', 5, 37.3947, 127.1111, 130000, 'HOTEL', '비즈니스,역세권,데이트', true);

-- 강원 (region_id = 6)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('강릉 세인트존스 호텔', '경포해변 인접 오션뷰 호텔', 'https://loremflickr.com/400/300/gangneung,ocean+hotel?lock=20', 6, 37.7886, 128.9177, 190000, 'HOTEL', '오션뷰,가족친화,반려동물동반,힐링', true),
('춘천 남이섬 펜션', '남이섬 선착장 인근 자연 펜션', 'https://loremflickr.com/400/300/nami+island,pension?lock=21', 6, 37.7919, 127.5261, 85000, 'PENSION', '자연,조용한,힐링,트레킹', true),
('속초 설악 리조트', '설악산 입구 가족 리조트', 'https://loremflickr.com/400/300/seoraksan,resort?lock=22', 6, 38.1901, 128.5909, 180000, 'RESORT', '숲세권,가족친화,자연,트레킹', true),
('평창 하늘목장 펜션', '해발 850m 청정 자연 속 힐링', 'https://loremflickr.com/400/300/pyeongchang,ranch+mountain?lock=23', 6, 37.6705, 128.7171, 95000, 'PENSION', '자연,힐링,조용한,트레킹', true);

-- 충북 (region_id = 7)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('수안보 상록호텔', '수안보 온천 단지 내 호텔', 'https://loremflickr.com/400/300/suanbo,hot+spring+hotel?lock=24', 7, 36.8506, 127.9888, 130000, 'HOTEL', '힐링,가족친화,조용한,체험', true),
('청주 라마다호텔', '청주 시내 중심 비즈니스 호텔', 'https://loremflickr.com/400/300/ramada+hotel+cheongju?lock=25', 7, 36.6354, 127.4897, 110000, 'HOTEL', '비즈니스,시내중심,데이트', true),
('충주호 레이크 펜션', '충주호 전망 조용한 힐링 펜션', 'https://loremflickr.com/400/300/chungju+lake,pension?lock=26', 7, 36.9750, 127.8834, 80000, 'PENSION', '호수뷰,조용한,자연,힐링', true);

-- 충남 (region_id = 8)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('천안 스파비스 호텔', '천안역 인접 비즈니스 호텔', 'https://loremflickr.com/400/300/cheonan,hotel?lock=27', 8, 36.8151, 127.1139, 120000, 'HOTEL', '비즈니스,역세권,힐링', true),
('태안 안면도 리조트', '안면도 해변 가족 리조트', 'https://loremflickr.com/400/300/anmyeondo,beach+resort?lock=28', 8, 36.5125, 126.3296, 160000, 'RESORT', '오션뷰,가족친화,체험,자연', true),
('아산 온천 호텔', '아산 온천 지구 내 힐링 숙소', 'https://loremflickr.com/400/300/asan,hot+spring+hotel?lock=29', 8, 36.7898, 127.0018, 140000, 'HOTEL', '힐링,조용한,체험', true);

-- 경북 (region_id = 9)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('경주 힐튼호텔', '보문관광단지 내 럭셔리 호텔', 'https://loremflickr.com/400/300/hilton+gyeongju?lock=30', 9, 35.8413, 129.2126, 200000, 'HOTEL', '럭셔리,전통/역사,호수뷰,데이트', true),
('포항 영일대 오션뷰 펜션', '영일대해수욕장 도보 3분', 'https://loremflickr.com/400/300/pohang,ocean+view+beach?lock=31', 9, 36.0563, 129.3780, 85000, 'PENSION', '오션뷰,가족친화,추억', true),
('경주 한옥스테이', '경주 교촌마을 내 전통 한옥', 'https://loremflickr.com/400/300/gyeongju,hanok?lock=32', 9, 35.8342, 129.2190, 70000, 'GUESTHOUSE', '전통/역사,조용한,체험,추억', true),
('안동 하회마을 한옥체험', '유네스코 세계유산 하회마을 숙박', 'https://loremflickr.com/400/300/hahoe+village,hanok?lock=33', 9, 36.5394, 128.5176, 80000, 'GUESTHOUSE', '전통/역사,추억,체험,힐링', true);

-- 경남 (region_id = 10)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('통영 스탠포드호텔', '통영 항구뷰 깔끔한 호텔', 'https://loremflickr.com/400/300/tongyeong,harbor+hotel?lock=34', 10, 34.8544, 128.4337, 150000, 'HOTEL', '오션뷰,비즈니스,데이트', true),
('거제 외도 펜션', '거제 바다 전망 가족 펜션', 'https://loremflickr.com/400/300/geoje,sea+pension?lock=35', 10, 34.8017, 128.6956, 100000, 'PENSION', '오션뷰,가족친화,자연', true),
('진주 동방호텔', '진주성 인근 시내 중심 호텔', 'https://loremflickr.com/400/300/jinju,city+hotel?lock=36', 10, 35.1906, 128.0847, 90000, 'HOTEL', '비즈니스,시내중심,전통/역사,예술', true);

-- 전북 (region_id = 11)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('전주 한옥마을 한옥스테이', '전주 한옥마을 중심 전통 숙소', 'https://loremflickr.com/400/300/jeonju+hanok+village?lock=37', 11, 35.8149, 127.1530, 75000, 'GUESTHOUSE', '전통/역사,체험,추억,힐링', true),
('군산 근대문화호텔', '근대 건축물 리모델링 이색 호텔', 'https://loremflickr.com/400/300/gunsan,modern+architecture?lock=38', 11, 35.9906, 126.7115, 100000, 'HOTEL', '전통/역사,가성비,추억,예술', true),
('무주 덕유산 리조트', '덕유산 스키장 단지 내 리조트', 'https://loremflickr.com/400/300/muju+deogyusan,ski+resort?lock=39', 11, 35.8921, 127.7410, 170000, 'RESORT', '숲세권,가족친화,스릴,자연', true);

-- 전남 (region_id = 12)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('여수 디오션 리조트', '여수 해상케이블카 인접 오션뷰', 'https://loremflickr.com/400/300/yeosu,ocean+resort?lock=40', 12, 34.7503, 127.7400, 220000, 'RESORT', '오션뷰,럭셔리,가족친화,데이트', true),
('순천 에코스테이', '순천만 습지 인근 친환경 숙소', 'https://loremflickr.com/400/300/suncheon,eco+stay?lock=41', 12, 34.9506, 127.4873, 70000, 'PENSION', '자연,힐링,조용한,트레킹', true),
('담양 대나무숲 펜션', '죽녹원 인근 조용한 힐링 펜션', 'https://loremflickr.com/400/300/damyang,bamboo+forest?lock=42', 12, 35.3214, 126.9886, 85000, 'PENSION', '숲세권,조용한,힐링,자연', true);

-- 제주 (region_id = 13)
INSERT INTO accommodation (name, description, image_url, region_id, latitude, longitude, price_per_night, type, keywords, is_active) VALUES
('제주 신라호텔', '중문관광단지 5성급 럭셔리 호텔', 'https://loremflickr.com/400/300/shilla+hotel+jeju?lock=43', 13, 33.2478, 126.4095, 300000, 'HOTEL', '럭셔리,오션뷰,데이트,추억', true),
('서귀포 올레 펜션', '올레길 7코스 인접 조용한 펜션', 'https://loremflickr.com/400/300/seogwipo,olle+trail?lock=44', 13, 33.2469, 126.5632, 90000, 'PENSION', '자연,조용한,트레킹,힐링', true),
('애월 감성 게스트하우스', '애월 카페거리 도보 5분', 'https://loremflickr.com/400/300/aewol,jeju+cafe?lock=45', 13, 33.4634, 126.3092, 50000, 'GUESTHOUSE', '오션뷰,가성비,데이트,예술', true),
('중문 색달 리조트', '중문 해수욕장 앞 가족 리조트', 'https://loremflickr.com/400/300/jungmun,resort+jeju?lock=46', 13, 33.2470, 126.4120, 230000, 'RESORT', '오션뷰,가족친화,힐링', true),
('성산 일출봉뷰 펜션', '성산일출봉 전망 오션뷰 펜션', 'https://loremflickr.com/400/300/seongsan+ilchulbong?lock=47', 13, 33.4584, 126.9415, 100000, 'PENSION', '오션뷰,자연,추억,체험', true);


-- ============================================================
-- 3. ACTIVITY (액티비티) - 45건
-- ============================================================

-- 서울 (1)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('한강 카약 체험', '여의도 한강공원 출발 카약 투어', 'https://loremflickr.com/400/300/han+river,kayak?lock=48', 1, 37.5172, 126.9350, 35000, 'WATER', '체험,시티뷰,데이트', 120, true),
('북촌 한옥마을 문화투어', '전문 해설사와 함께하는 한옥마을 투어', 'https://loremflickr.com/400/300/bukchon+hanok+village?lock=49', 1, 37.5826, 126.9831, 25000, 'CULTURE', '전통/역사,체험,추억', 90, true),
('이태원 월드쿠킹 클래스', '외국 셰프에게 배우는 세계 요리', 'https://loremflickr.com/400/300/cooking+class,korean?lock=50', 1, 37.5340, 126.9948, 55000, 'FOOD', '체험,데이트', 150, true),
('남산 야경 트래킹', 'N서울타워 코스 야간 트래킹', 'https://loremflickr.com/400/300/namsan+tower,night?lock=51', 1, 37.5511, 126.9828, 15000, 'OUTDOOR', '시티뷰,트레킹,데이트', 120, true);

-- 부산 (3)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('해운대 서핑 체험', '초보자 맞춤 서핑 강습 포함', 'https://loremflickr.com/400/300/haeundae,surfing?lock=52', 3, 35.1587, 129.1605, 45000, 'WATER', '스릴,체험,오션뷰', 120, true),
('감천문화마을 아트투어', '부산의 마추픽추 골목 투어', 'https://loremflickr.com/400/300/gamcheon+culture+village?lock=53', 3, 35.0972, 129.0104, 20000, 'CULTURE', '예술,추억,체험', 90, true),
('자갈치시장 먹방 투어', '부산 대표 해산물 시장 미식 투어', 'https://loremflickr.com/400/300/jagalchi+market?lock=54', 3, 35.0969, 129.0306, 30000, 'FOOD', '전통/역사,시내중심,체험', 120, true),
('해운대 요트 투어', '광안대교 야경 프라이빗 요트', 'https://loremflickr.com/400/300/haeundae,yacht?lock=55', 3, 35.1550, 129.1492, 55000, 'WATER', '오션뷰,럭셔리,데이트', 60, true);

-- 인천 (2)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('월미도 테마파크 체험', '월미도 놀이공원 자유이용', 'https://loremflickr.com/400/300/wolmido,theme+park?lock=56', 2, 37.4753, 126.5977, 15000, 'OUTDOOR', '스릴,가족친화,오션뷰', 60, true),
('강화도 갯벌 체험', '강화 동막해변 갯벌 생태 체험', 'https://loremflickr.com/400/300/ganghwado,mudflat?lock=57', 2, 37.7471, 126.4878, 20000, 'OUTDOOR', '체험,자연,가족친화', 120, true),
('인천 차이나타운 먹방투어', '중화거리 대표 맛집 코스 투어', 'https://loremflickr.com/400/300/incheon+chinatown,food?lock=58', 2, 37.4740, 126.6178, 25000, 'FOOD', '전통/역사,체험,시내중심', 90, true);

-- 울산 (4)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('간절곶 일출 트래킹', '한국 최동단 일출 명소 트래킹', 'https://loremflickr.com/400/300/ganjeolgot,sunrise?lock=59', 4, 35.3616, 129.3604, 10000, 'OUTDOOR', '트레킹,오션뷰,자연', 90, true),
('대왕암공원 해안 산책', '기암괴석 해안 둘레길 산책', 'https://loremflickr.com/400/300/daewangam,coast?lock=60', 4, 35.4399, 129.4350, 0, 'OUTDOOR', '오션뷰,자연,트레킹', 120, true),
('울산 고래문화 체험', '장생포 고래박물관 체험 프로그램', 'https://loremflickr.com/400/300/ulsan,whale+museum?lock=61', 4, 35.4907, 129.3871, 15000, 'CULTURE', '체험,가족친화,전통/역사', 60, true);

-- 경기 (5)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('수원화성 야간투어', '수원화성 야간 조명 해설 투어', 'https://loremflickr.com/400/300/suwon+hwaseong,night?lock=62', 5, 37.2876, 127.0137, 15000, 'CULTURE', '전통/역사,시티뷰,데이트', 90, true),
('용인 서바이벌 게임', '실외 서바이벌 팀 배틀 체험', 'https://loremflickr.com/400/300/survival+game,outdoor?lock=63', 5, 37.2820, 127.1975, 30000, 'OUTDOOR', '스릴,체험,가족친화', 120, true),
('이천 도자기 만들기 체험', '이천 도자기마을 핸드메이드 체험', 'https://loremflickr.com/400/300/icheon,pottery?lock=64', 5, 37.2792, 127.4352, 25000, 'CRAFT', '체험,예술,추억', 120, true),
('가평 번지점프', '가평 북한강 50m 번지점프', 'https://loremflickr.com/400/300/gapyeong,bungee+jump?lock=65', 5, 37.8151, 127.5157, 40000, 'OUTDOOR', '스릴,자연,체험', 30, true);

-- 강원 (6)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('춘천 물레길 카누 체험', '의암호 물레길 카누 투어', 'https://loremflickr.com/400/300/chuncheon,canoe+lake?lock=66', 6, 37.8813, 127.7301, 30000, 'WATER', '호수뷰,자연,힐링', 120, true),
('강릉 서핑 체험', '사천해변 초보 서핑 강습', 'https://loremflickr.com/400/300/gangneung,surfing?lock=67', 6, 37.7730, 128.9468, 40000, 'WATER', '스릴,체험,오션뷰', 120, true),
('정선 레일바이크', '정선 아우라지 레일바이크 코스', 'https://loremflickr.com/400/300/jeongseon,railbike?lock=68', 6, 37.3802, 128.6608, 25000, 'OUTDOOR', '자연,추억,가족친화', 60, true),
('인제 래프팅', '내린천 급류 래프팅 체험', 'https://loremflickr.com/400/300/inje,rafting?lock=69', 6, 38.0646, 128.1703, 35000, 'WATER', '스릴,자연,체험', 150, true);

-- 충북 (7)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('단양 패러글라이딩', '소백산 절경 위 패러글라이딩', 'https://loremflickr.com/400/300/danyang,paragliding?lock=70', 7, 36.9859, 128.3656, 80000, 'OUTDOOR', '스릴,자연,체험', 30, true),
('수안보 온천 체험', '수안보 온천수 노천탕 체험', 'https://loremflickr.com/400/300/suanbo,hot+spring?lock=71', 7, 36.8506, 127.9888, 12000, 'CULTURE', '힐링,체험,전통/역사', 120, true),
('충주호 유람선', '충주호 절경 유람선 투어', 'https://loremflickr.com/400/300/chungju+lake,cruise?lock=72', 7, 36.9750, 127.8834, 18000, 'WATER', '호수뷰,자연,힐링', 60, true);

-- 충남 (8)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('태안 서핑 체험', '만리포 해변 서핑 강습', 'https://loremflickr.com/400/300/taean,surfing+beach?lock=73', 8, 36.5824, 126.3021, 40000, 'WATER', '스릴,오션뷰,체험', 120, true),
('공주 백제문화 체험', '공주 백제문화단지 역사 체험', 'https://loremflickr.com/400/300/gongju,baekje+culture?lock=74', 8, 36.4627, 127.1188, 15000, 'CULTURE', '전통/역사,체험,가족친화', 90, true),
('보령 머드 체험', '대천해수욕장 머드 축제 체험', 'https://loremflickr.com/400/300/boryeong,mud+festival?lock=75', 8, 36.3341, 126.5084, 20000, 'OUTDOOR', '체험,오션뷰,추억', 120, true);

-- 경북 (9)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('경주 자전거 역사투어', '대릉원~첨성대~안압지 자전거 코스', 'https://loremflickr.com/400/300/gyeongju,bicycle+tour?lock=76', 9, 35.8342, 129.2190, 15000, 'CULTURE', '전통/역사,트레킹,추억', 180, true),
('포항 호미곶 일출투어', '한반도 최동단 호미곶 일출 감상', 'https://loremflickr.com/400/300/homigot,sunrise?lock=77', 9, 36.0770, 129.5678, 10000, 'OUTDOOR', '오션뷰,자연,힐링', 120, true),
('안동 하회마을 탈춤 체험', '하회별신굿탈놀이 체험 프로그램', 'https://loremflickr.com/400/300/hahoe,mask+dance?lock=78', 9, 36.5394, 128.5176, 20000, 'CULTURE', '전통/역사,체험,예술', 90, true);

-- 경남 (10)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('통영 케이블카 & 미륵산', '한려수도 전망 케이블카 + 트래킹', 'https://loremflickr.com/400/300/tongyeong,cable+car?lock=79', 10, 34.8175, 128.4453, 15000, 'OUTDOOR', '오션뷰,트레킹,자연', 120, true),
('거제 해금강 유람선', '해금강 절경 유람선 투어', 'https://loremflickr.com/400/300/geoje+haegumgang,cruise?lock=80', 10, 34.7361, 128.6949, 22000, 'WATER', '오션뷰,자연,힐링', 60, true),
('남해 독일마을 투어', '이국적 독일마을 산책 & 맥주 체험', 'https://loremflickr.com/400/300/namhae,german+village?lock=81', 10, 34.7633, 127.8978, 10000, 'CULTURE', '데이트,추억', 90, true);

-- 전북 (11)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('전주 한복체험 & 한옥마을 투어', '한복 대여 + 한옥마을 자유 투어', 'https://loremflickr.com/400/300/jeonju,hanbok?lock=82', 11, 35.8149, 127.1530, 20000, 'CULTURE', '전통/역사,체험,데이트', 180, true),
('군산 시간여행 투어', '근대 건축물 골목 해설 투어', 'https://loremflickr.com/400/300/gunsan,retro+architecture?lock=83', 11, 35.9906, 126.7115, 15000, 'CULTURE', '전통/역사,추억,체험', 120, true),
('무주 래프팅', '금강 상류 래프팅 체험', 'https://loremflickr.com/400/300/muju,rafting?lock=84', 11, 35.9155, 127.6607, 30000, 'WATER', '스릴,자연,체험', 150, true);

-- 전남 (12)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('여수 해상케이블카', '돌산~자산 해상 케이블카 왕복', 'https://loremflickr.com/400/300/yeosu,cable+car+sea?lock=85', 12, 34.7356, 127.7400, 15000, 'OUTDOOR', '오션뷰,데이트,스릴', 30, true),
('순천만 갈대밭 생태투어', '순천만 습지 해설사 동행 투어', 'https://loremflickr.com/400/300/suncheonman,wetland+reed?lock=86', 12, 34.8870, 127.5092, 8000, 'OUTDOOR', '자연,힐링,트레킹', 120, true),
('보성 녹차밭 체험', '대한다원 녹차 따기 & 다도 체험', 'https://loremflickr.com/400/300/boseong,green+tea+field?lock=87', 12, 34.7332, 127.0800, 5000, 'CULTURE', '힐링,자연,체험', 90, true);

-- 제주 (13)
INSERT INTO activity (name, description, image_url, region_id, latitude, longitude, price, category, keywords, duration_minutes, is_active) VALUES
('제주 스쿠버다이빙', '서귀포 문섬 스쿠버다이빙 체험', 'https://loremflickr.com/400/300/jeju,scuba+diving?lock=88', 13, 33.5142, 126.5297, 60000, 'WATER', '스릴,오션뷰,체험', 120, true),
('한라산 트래킹', '한라산 영실코스 트래킹', 'https://loremflickr.com/400/300/hallasan,hiking?lock=89', 13, 33.3618, 126.5290, 0, 'OUTDOOR', '트레킹,자연', 300, true),
('제주 승마 체험', '제주 조랑말 초원 승마 체험', 'https://loremflickr.com/400/300/jeju,horse+riding?lock=90', 13, 33.3901, 126.2661, 35000, 'OUTDOOR', '자연,체험,가족친화', 60, true),
('서귀포 감귤따기 체험', '제주 감귤농장 수확 체험', 'https://loremflickr.com/400/300/jeju,tangerine+farm?lock=91', 13, 33.2543, 126.5150, 15000, 'CULTURE', '체험,가족친화,추억', 60, true),
('우도 전기자전거 투어', '우도 해안 일주 전기자전거', 'https://loremflickr.com/400/300/udo+island,bicycle?lock=92', 13, 33.5028, 126.9511, 20000, 'OUTDOOR', '오션뷰,자연,데이트', 180, true);


-- ============================================================
-- 4. TICKET (티켓) - 32건
-- ============================================================

-- 서울 (1)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('국립중앙박물관 특별전', '한국 고대 문명 특별 기획전', 'https://loremflickr.com/400/300/national+museum+korea?lock=93', 1, 37.5239, 126.9806, 8000, 'MUSEUM', '전통/역사,체험,가성비', '2026-01-01', '2026-12-31', true),
('COEX 아쿠아리움', '삼성동 코엑스 대형 아쿠아리움', 'https://loremflickr.com/400/300/coex+aquarium+seoul?lock=94', 1, 37.5115, 127.0590, 28000, 'THEMEPARK', '가족친화,체험', '2026-01-01', '2026-12-31', true),
('뮤지컬 명성황후', '대학로 뮤지컬 공연 R석', 'https://loremflickr.com/400/300/musical+theater+korea?lock=95', 1, 37.5663, 126.9779, 66000, 'PERFORMANCE', '예술,데이트,전통/역사', '2026-03-01', '2026-08-31', true);

-- 부산 (3)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('SEA LIFE 부산 아쿠아리움', '해운대 해저터널 대형 아쿠아리움', 'https://loremflickr.com/400/300/sealife+busan+aquarium?lock=96', 3, 35.1587, 129.1605, 25000, 'THEMEPARK', '가족친화,오션뷰,체험', '2026-01-01', '2026-12-31', true),
('부산시립미술관', '현대미술 기획전시 관람', 'https://loremflickr.com/400/300/busan+art+museum?lock=97', 3, 35.1545, 129.1187, 5000, 'MUSEUM', '예술,가성비,데이트', '2026-01-01', '2026-12-31', true),
('용두산공원 부산타워', '부산항 360도 전망대', 'https://loremflickr.com/400/300/busan+tower?lock=98', 3, 35.1008, 129.0324, 12000, 'EXHIBITION', '시티뷰,오션뷰,추억', '2026-01-01', '2026-12-31', true);

-- 인천 (2)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('파라다이스시티 원더박스', '송도 실내 테마파크', 'https://loremflickr.com/400/300/paradise+city+incheon?lock=99', 2, 37.3842, 126.6652, 35000, 'THEMEPARK', '가족친화,스릴,럭셔리', '2026-01-01', '2026-12-31', true),
('인천 개항박물관', '인천 개항 역사 전시', 'https://loremflickr.com/400/300/incheon,port+museum?lock=100', 2, 37.4745, 126.6176, 3000, 'MUSEUM', '전통/역사,가성비,추억', '2026-01-01', '2026-12-31', true);

-- 울산 (4)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('울산대공원 장미원', '국내 최대 장미 테마 정원', 'https://loremflickr.com/400/300/ulsan,rose+garden?lock=101', 4, 35.5342, 129.2576, 3000, 'EXHIBITION', '힐링,자연,가족친화', '2026-04-01', '2026-10-31', true),
('울산박물관', '울산 역사문화 상설전시', 'https://loremflickr.com/400/300/ulsan,museum?lock=102', 4, 35.5413, 129.2862, 0, 'MUSEUM', '전통/역사,가성비,체험', '2026-01-01', '2026-12-31', true);

-- 경기 (5)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('에버랜드 자유이용권', '용인 에버랜드 종일 자유이용', 'https://loremflickr.com/400/300/everland+theme+park?lock=103', 5, 37.2942, 127.2022, 56000, 'THEMEPARK', '스릴,가족친화,체험', '2026-01-01', '2026-12-31', true),
('한국민속촌', '조선시대 전통문화 체험마을', 'https://loremflickr.com/400/300/korean+folk+village?lock=104', 5, 37.2590, 127.1194, 25000, 'THEMEPARK', '전통/역사,체험,가족친화', '2026-01-01', '2026-12-31', true),
('경기도미술관', '현대미술 기획 전시', 'https://loremflickr.com/400/300/gyeonggi,art+museum?lock=105', 5, 37.3875, 127.0913, 4000, 'MUSEUM', '예술,가성비,힐링', '2026-01-01', '2026-12-31', true);

-- 강원 (6)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('강릉 오죽헌', '신사임당 율곡 이이 유적지', 'https://loremflickr.com/400/300/ojukheon+gangneung?lock=106', 6, 37.7788, 128.8774, 3000, 'MUSEUM', '전통/역사,자연,가성비', '2026-01-01', '2026-12-31', true),
('속초 설악워터피아', '설악산 온천수 워터파크', 'https://loremflickr.com/400/300/seorak,waterpark?lock=107', 6, 38.1651, 128.5663, 42000, 'THEMEPARK', '스릴,가족친화,숲세권', '2026-06-01', '2026-09-30', true),
('춘천 애니메이션박물관', '국내 유일 애니메이션 전문 박물관', 'https://loremflickr.com/400/300/chuncheon,animation+museum?lock=108', 6, 37.8607, 127.7150, 10000, 'MUSEUM', '가족친화,체험,예술', '2026-01-01', '2026-12-31', true);

-- 충북 (7)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('청주 국립현대미술관', '현대미술 대규모 기획전', 'https://loremflickr.com/400/300/cheongju,modern+art+museum?lock=109', 7, 36.6095, 127.4756, 5000, 'MUSEUM', '예술,시내중심,가성비', '2026-01-01', '2026-12-31', true),
('단양 고수동굴', '천연기념물 석회암 동굴 탐험', 'https://loremflickr.com/400/300/danyang,limestone+cave?lock=110', 7, 36.9722, 128.3543, 4000, 'EXHIBITION', '자연,체험,스릴', '2026-01-01', '2026-12-31', true);

-- 충남 (8)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('독립기념관', '독립운동 역사 전시관', 'https://loremflickr.com/400/300/independence+hall+korea?lock=111', 8, 36.7942, 127.0906, 0, 'MUSEUM', '전통/역사,가성비,체험', '2026-01-01', '2026-12-31', true),
('천안 상록리조트 워터파크', '천안 대형 실내외 워터파크', 'https://loremflickr.com/400/300/cheonan,waterpark?lock=112', 8, 36.8080, 127.1452, 38000, 'THEMEPARK', '가족친화,스릴,루프탑/야외', '2026-05-01', '2026-09-30', true);

-- 경북 (9)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('경주월드 어뮤즈먼트', '경주 대표 놀이공원', 'https://loremflickr.com/400/300/gyeongju+world,amusement+park?lock=113', 9, 35.8370, 129.2888, 42000, 'THEMEPARK', '스릴,가족친화,체험', '2026-01-01', '2026-12-31', true),
('국립경주박물관', '신라 천년 역사 유물 전시', 'https://loremflickr.com/400/300/gyeongju+national+museum?lock=114', 9, 35.8339, 129.2267, 0, 'MUSEUM', '전통/역사,가성비,체험', '2026-01-01', '2026-12-31', true),
('포항 스페이스워크', '환호공원 나선형 전망 조형물', 'https://loremflickr.com/400/300/pohang+space+walk?lock=115', 9, 36.0191, 129.3429, 0, 'EXHIBITION', '예술,오션뷰,스릴', '2026-01-01', '2026-12-31', true);

-- 경남 (10)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('통영 동피랑마을', '알록달록 벽화 예술마을', 'https://loremflickr.com/400/300/dongpirang,mural+village?lock=116', 10, 34.8452, 128.4283, 0, 'EXHIBITION', '예술,오션뷰,추억', '2026-01-01', '2026-12-31', true),
('진주성', '임진왜란 역사 유적지', 'https://loremflickr.com/400/300/jinjuseong,fortress?lock=117', 10, 35.1921, 128.0805, 2000, 'MUSEUM', '전통/역사,가성비,시내중심', '2026-01-01', '2026-12-31', true);

-- 전북 (11)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('전주 국립무형유산원', '한국 무형문화재 전시 체험', 'https://loremflickr.com/400/300/jeonju,intangible+heritage?lock=118', 11, 35.8126, 127.1518, 0, 'MUSEUM', '전통/역사,체험,가성비', '2026-01-01', '2026-12-31', true),
('무주 덕유산리조트 스키장', '덕유산 슬로프 스키 리프트권', 'https://loremflickr.com/400/300/muju,ski+resort?lock=119', 11, 35.8921, 127.7410, 65000, 'THEMEPARK', '스릴,가족친화,숲세권', '2025-12-01', '2026-03-15', true);

-- 전남 (12)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('여수 아쿠아플라넷', '여수 엑스포 대형 아쿠아리움', 'https://loremflickr.com/400/300/yeosu+aquaplanet?lock=120', 12, 34.7472, 127.7380, 29000, 'THEMEPARK', '가족친화,오션뷰,체험', '2026-01-01', '2026-12-31', true),
('순천만국가정원', '대한민국 제1호 국가정원', 'https://loremflickr.com/400/300/suncheon+national+garden?lock=121', 12, 34.9274, 127.4999, 8000, 'EXHIBITION', '자연,힐링,가족친화', '2026-01-01', '2026-12-31', true);

-- 제주 (13)
INSERT INTO ticket (name, description, image_url, region_id, latitude, longitude, price, category, keywords, available_from, available_to, is_active) VALUES
('테디베어뮤지엄', '중문 테디베어 테마 뮤지엄', 'https://loremflickr.com/400/300/teddy+bear+museum+jeju?lock=122', 13, 33.2505, 126.4104, 12000, 'MUSEUM', '가족친화,체험,추억', '2026-01-01', '2026-12-31', true),
('아쿠아플라넷 제주', '성산 대형 아쿠아리움', 'https://loremflickr.com/400/300/aquaplanet+jeju?lock=123', 13, 33.4343, 126.9271, 40000, 'THEMEPARK', '가족친화,오션뷰,체험', '2026-01-01', '2026-12-31', true),
('제주유리의성', '유리공예 전시 & 체험', 'https://loremflickr.com/400/300/jeju,glass+castle?lock=124', 13, 33.3103, 126.2738, 11000, 'EXHIBITION', '예술,체험,자연', '2026-01-01', '2026-12-31', true);


-- ============================================================
-- 5. EVENT (축제/행사) - 14건
-- ============================================================

INSERT IGNORE INTO event (content_id, name, address, addr2, zip_code, tel, image_url, description, cat1, cat2, cat3, start_date, end_date, map_x, map_y) VALUES
(10001, '진해군항제', '경상남도 창원시 진해구 중원로 일원', '', '51601', '055-225-3691', 'https://loremflickr.com/400/300/cherry+blossom+festival?lock=201', '대한민국 최대 벚꽃축제, 매년 4월 진해에서 개최됩니다.', 'A02', '축제', 'A02080100', '20260401', '20260410', 128.7057, 35.1344),
(10002, '보령머드축제', '충청남도 보령시 대천해수욕장', '', '33483', '041-930-3882', 'https://loremflickr.com/400/300/mud+festival?lock=202', '머드를 이용한 다양한 체험 프로그램이 가득한 여름 축제입니다.', 'A02', '축제', 'A02080600', '20260715', '20260724', 126.4994, 36.3216),
(10003, '부산불꽃축제', '부산광역시 수영구 광안리해수욕장', '', '48302', '051-780-0012', 'https://loremflickr.com/400/300/fireworks+festival?lock=203', '광안대교를 배경으로 화려한 불꽃쇼가 펼쳐지는 축제입니다.', 'A02', '축제', 'A02080200', '20261024', '20261024', 129.1187, 35.1532),
(10004, '제주들불축제', '제주특별자치도 제주시 애월읍 봉성리', '', '63041', '064-728-2751', 'https://loremflickr.com/400/300/jeju+fire+festival?lock=204', '제주 전통 방목 문화에서 유래한 들불놓기 축제입니다.', 'A02', '축제', 'A02080100', '20260301', '20260303', 126.3839, 33.4312),
(10005, '안동국제탈춤페스티벌', '경상북도 안동시 탈춤공원로 일원', '', '36703', '054-840-6397', 'https://loremflickr.com/400/300/mask+dance+festival?lock=205', '전통 탈춤과 세계 각국의 가면극을 만나볼 수 있는 축제입니다.', 'A02', '축제', 'A02081300', '20260925', '20261004', 128.7276, 36.5614),
(10006, '전주비빔밥축제', '전라북도 전주시 완산구 한옥마을 일원', '', '55043', '063-281-2114', 'https://loremflickr.com/400/300/bibimbap+festival?lock=206', '전주 대표 음식 비빔밥을 테마로 한 음식 축제입니다.', 'A05', '먹거리', 'A05020100', '20261015', '20261018', 127.1520, 35.8154),
(10007, '강경젓갈축제', '충청남도 논산시 강경읍 젓갈골목', '', '32903', '041-730-3224', 'https://loremflickr.com/400/300/salted+seafood+festival?lock=207', '100년 전통의 젓갈을 맛보고 구매할 수 있는 축제입니다.', 'A05', '먹거리', 'A05020300', '20261010', '20261013', 126.9974, 36.1659),
(10008, '이천쌀문화축제', '경기도 이천시 설봉공원 일원', '', '17379', '031-644-4125', 'https://loremflickr.com/400/300/rice+festival?lock=208', '임금님께 진상된 이천쌀을 주제로 한 문화축제입니다.', 'A05', '먹거리', 'A05020100', '20261022', '20261025', 127.4351, 37.2798),
(10009, '남도음식문화축제', '전라남도 목포시 삼학도 일원', '', '58741', '061-270-8252', 'https://loremflickr.com/400/300/namdo+food+festival?lock=209', '남도의 맛을 한자리에서 즐길 수 있는 음식 축제입니다.', 'A05', '먹거리', 'A05020900', '20261105', '20261108', 126.3826, 34.7880),
(10010, '태백산눈축제', '강원특별자치도 태백시 태백산로 일원', '', '26023', '033-550-2828', 'https://loremflickr.com/400/300/snow+festival?lock=210', '겨울철 눈조각과 다양한 겨울 체험이 가능한 축제입니다.', 'A02', '시즌테마', 'A02070200', '20260120', '20260202', 128.9882, 37.0954),
(10011, '화천산천어축제', '강원특별자치도 화천군 화천읍 선등거리', '', '24312', '033-440-2575', 'https://loremflickr.com/400/300/ice+fishing+festival?lock=211', '얼음낚시와 맨손잡기 등 겨울 레저의 진수를 경험할 수 있습니다.', 'A02', '시즌테마', 'A02070200', '20260110', '20260201', 127.7062, 38.1123),
(10012, '서울빛초롱축제', '서울특별시 종로구 청계천로', '', '03191', '02-2290-7111', 'https://loremflickr.com/400/300/lantern+festival+seoul?lock=212', '청계천을 수놓는 아름다운 등불축제입니다.', 'A02', '시즌테마', 'A02070100', '20261101', '20261117', 126.9996, 37.5696),
(10013, '에버랜드튤립축제', '경기도 용인시 처인구 에버랜드로', '', '17023', '031-320-5000', 'https://loremflickr.com/400/300/tulip+festival?lock=213', '100만 송이 튤립이 만개하는 봄맞이 플라워 축제입니다.', 'A02', '시즌테마', 'A02070100', '20260320', '20260426', 127.1997, 37.2942),
(10014, '부산크리스마스트리문화축제', '부산광역시 중구 광복로 일원', '', '48937', '051-600-4082', 'https://loremflickr.com/400/300/christmas+tree+festival?lock=214', '화려한 트리와 조명으로 겨울밤을 밝히는 축제입니다.', 'A02', '시즌테마', 'A02070100', '20261201', '20270101', 129.0297, 35.1007);


-- ============================================================
-- 6. TRAVEL_SPOT LEVEL 업데이트 (가챠용)
-- ============================================================

UPDATE travel_spot
SET level = CASE
    WHEN name REGEXP '산$|악$|능선|대피소|숲길' THEN 3
    WHEN name REGEXP '섬$|계곡|폭포|사$|동굴|해수욕장' THEN 2
    WHEN name REGEXP '공원|거리|광장|박물관|미술관|시장' THEN 1
    ELSE FLOOR(1 + RAND() * 3)
END
WHERE level IS NULL;


-- ============================================================
-- 완료! 서버 재시작하면 KeywordBatchService가 자동으로
-- spot_keyword / accommodation.keywords / activity.keywords / ticket.keywords 매핑
-- ============================================================

SELECT '=== init-data.sql 실행 완료 ===' AS result;
SELECT 'keyword' AS tbl, COUNT(*) AS cnt FROM keyword
UNION ALL SELECT 'accommodation', COUNT(*) FROM accommodation
UNION ALL SELECT 'activity', COUNT(*) FROM activity
UNION ALL SELECT 'ticket', COUNT(*) FROM ticket
UNION ALL SELECT 'event (mock)', COUNT(*) FROM event WHERE content_id >= 10001
UNION ALL SELECT 'travel_spot (with level)', COUNT(*) FROM travel_spot WHERE level IS NOT NULL;
