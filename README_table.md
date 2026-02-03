# team1-boot
-- =============================================
-- 1. 회원 테이블
-- =============================================
CREATE TABLE users (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '회원 고유번호',
username VARCHAR(50) NOT NULL UNIQUE COMMENT '아이디',
password VARCHAR(100) NOT NULL COMMENT '비밀번호 (암호화)',
email VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일',
phone VARCHAR(20) COMMENT '연락처',
role VARCHAR(20) DEFAULT 'USER' COMMENT '권한 (USER/ADMIN)',
keyword_pref VARCHAR(200) COMMENT '여행 성향 키워드 (쉼표 구분)',
point INT DEFAULT 0 COMMENT '보유 포인트',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT '회원 정보';

-- =============================================
-- 2. 지역 마스터 테이블
-- =============================================
CREATE TABLE region (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '지역 고유번호',
name VARCHAR(50) NOT NULL UNIQUE COMMENT '지역명 (예: 서울, 부산, 제주)',
parent_id BIGINT COMMENT '상위 지역 ID (시/도 → 시/군/구)',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
FOREIGN KEY (parent_id) REFERENCES region(id)
) COMMENT '지역 마스터';

-- =============================================
-- 3. 키워드/태그 마스터 테이블
-- =============================================
CREATE TABLE keyword (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '키워드 고유번호',
name VARCHAR(50) NOT NULL UNIQUE COMMENT '키워드명 (예: 힐링, 액티비티, 맛집)',
category VARCHAR(30) COMMENT '분류 (성향/테마/시설 등)',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT '키워드/태그 마스터';

-- =============================================
-- 4. 여행지(장소) 테이블
-- =============================================
CREATE TABLE travel_spot (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '여행지 고유번호',
name VARCHAR(100) NOT NULL COMMENT '여행지명',
region_id BIGINT COMMENT '지역 FK',
address VARCHAR(200) COMMENT '주소',
latitude DECIMAL(10, 7) COMMENT '위도',
longitude DECIMAL(10, 7) COMMENT '경도',
category VARCHAR(50) COMMENT '카테고리 (맛집/관광/숙소/액티비티 등)',
description TEXT COMMENT '설명',
avg_price INT COMMENT '평균 비용',
avg_time INT COMMENT '평균 소요 시간(분)',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
FOREIGN KEY (region_id) REFERENCES region(id)
) COMMENT '여행지(장소) 정보';

-- =============================================
-- 5. 여행지 이미지 테이블
-- =============================================
CREATE TABLE spot_image (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '이미지 고유번호',
spot_id BIGINT NOT NULL COMMENT '여행지 FK',
file_name VARCHAR(200) NOT NULL COMMENT '파일명',
url VARCHAR(300) NOT NULL COMMENT '이미지 URL',
is_thumbnail BOOLEAN DEFAULT FALSE COMMENT '썸네일 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
FOREIGN KEY (spot_id) REFERENCES travel_spot(id) ON DELETE CASCADE
) COMMENT '여행지 이미지';

-- =============================================
-- 6. 여행지-키워드 매핑 테이블
-- =============================================
CREATE TABLE spot_keyword (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '매핑 고유번호',
spot_id BIGINT NOT NULL COMMENT '여행지 FK',
keyword_id BIGINT NOT NULL COMMENT '키워드 FK',
FOREIGN KEY (spot_id) REFERENCES travel_spot(id) ON DELETE CASCADE,
FOREIGN KEY (keyword_id) REFERENCES keyword(id) ON DELETE CASCADE
) COMMENT '여행지-키워드 매핑';

-- =============================================
-- 7. 일정 템플릿 테이블 (관리자용)
-- =============================================
CREATE TABLE plan_template (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '템플릿 고유번호',
name VARCHAR(100) NOT NULL COMMENT '템플릿명',
region_id BIGINT COMMENT '지역 FK',
description TEXT COMMENT '설명',
difficulty VARCHAR(10) COMMENT '난이도 (쉬움/보통/어려움)',
duration_days INT COMMENT '일정 기간 (일 수)',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
FOREIGN KEY (region_id) REFERENCES region(id)
) COMMENT '일정 템플릿 (관리자용)';

-- =============================================
-- 8. 여행 계획 테이블 (일반/랜덤/이벤트)
-- =============================================
CREATE TABLE travel_plan (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '계획 고유번호',
user_id BIGINT NOT NULL COMMENT '회원 FK',
template_id BIGINT COMMENT '사용된 템플릿 FK',
type VARCHAR(20) NOT NULL COMMENT '계획 유형 (일반/랜덤/이벤트)',
title VARCHAR(100) COMMENT '계획 제목',
region_id BIGINT COMMENT '지역 FK',
keyword VARCHAR(100) COMMENT '생성 시 사용된 키워드',
difficulty VARCHAR(10) COMMENT '난이도 (랜덤: 쉬움/보통/어려움)',
event_id BIGINT COMMENT '이벤트 FK (이벤트 기반일 경우)',
people_count INT DEFAULT 1 COMMENT '인원 수',
budget_min INT COMMENT '예산 범위 (최소)',
budget_max INT COMMENT '예산 범위 (최대)',
travel_date DATE COMMENT '여행 시작일',
duration_days INT COMMENT '여행 기간 (일 수)',
total_price INT COMMENT '총 예상 비용',
status VARCHAR(20) DEFAULT 'READY' COMMENT '상태 (READY/PAID/CANCEL/DONE)',
re_random_count INT DEFAULT 0 COMMENT '재랜덤 횟수',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
FOREIGN KEY (user_id) REFERENCES users(id),
FOREIGN KEY (template_id) REFERENCES plan_template(id),
FOREIGN KEY (region_id) REFERENCES region(id),
FOREIGN KEY (event_id) REFERENCES event(id)
) COMMENT '여행 계획';

-- =============================================
-- 9. 여행 일정 상세 테이블
-- =============================================
CREATE TABLE plan_detail (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '상세 고유번호',
plan_id BIGINT NOT NULL COMMENT '계획 FK',
spot_id BIGINT COMMENT '방문 장소 FK',
checklist_id BIGINT COMMENT '체크리스트 항목 FK',
day INT COMMENT '일차 (1일차, 2일차...)',
order_num INT COMMENT '일정 순서',
price INT COMMENT '예상 비용',
start_time TIME COMMENT '시작 시간',
end_time TIME COMMENT '종료 시간',
is_selected BOOLEAN DEFAULT TRUE COMMENT '결제 포함 여부',
FOREIGN KEY (plan_id) REFERENCES travel_plan(id) ON DELETE CASCADE,
FOREIGN KEY (spot_id) REFERENCES travel_spot(id),
FOREIGN KEY (checklist_id) REFERENCES checklist(id)
) COMMENT '여행 일정 상세';

-- =============================================
-- 10. 체크리스트 항목 테이블
-- =============================================
CREATE TABLE checklist (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '항목 고유번호',
name VARCHAR(50) NOT NULL COMMENT '항목명 (숙소/교통/액티비티/식사/티켓)',
category VARCHAR(30) COMMENT '카테고리',
default_price INT COMMENT '기본 예상 가격',
is_required BOOLEAN DEFAULT FALSE COMMENT '필수 항목 여부',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부'
) COMMENT '체크리스트 항목';

-- =============================================
-- 11. 결제 테이블
-- =============================================
CREATE TABLE payment (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '결제 고유번호',
user_id BIGINT NOT NULL COMMENT '회원 FK',
plan_id BIGINT NOT NULL COMMENT '계획 FK',
amount INT NOT NULL COMMENT '결제 금액',
payment_method VARCHAR(30) COMMENT '결제 수단 (카드/계좌이체/포인트 등)',
status VARCHAR(20) DEFAULT 'PAID' COMMENT '결제 상태 (PAID/CANCEL/REFUND)',
paid_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '결제일시',
FOREIGN KEY (user_id) REFERENCES users(id),
FOREIGN KEY (plan_id) REFERENCES travel_plan(id)
) COMMENT '결제 내역';

-- =============================================
-- 12. 후기 테이블
-- =============================================
CREATE TABLE review (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '후기 고유번호',
user_id BIGINT NOT NULL COMMENT '회원 FK',
plan_id BIGINT NOT NULL COMMENT '계획 FK',
content TEXT NOT NULL COMMENT '후기 내용',
rating INT CHECK (rating BETWEEN 1 AND 5) COMMENT '평점 (1~5)',
difficulty_score INT CHECK (difficulty_score BETWEEN 1 AND 5) COMMENT '난이도 체감 점수 (1~5)',
is_random BOOLEAN DEFAULT FALSE COMMENT '랜덤 여행 후기 여부',
is_public BOOLEAN DEFAULT TRUE COMMENT '공개 여부',
view_count INT DEFAULT 0 COMMENT '조회수',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
FOREIGN KEY (user_id) REFERENCES users(id),
FOREIGN KEY (plan_id) REFERENCES travel_plan(id)
) COMMENT '여행 후기';

-- =============================================
-- 13. 후기 이미지 테이블
-- =============================================
CREATE TABLE review_image (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '이미지 고유번호',
review_id BIGINT NOT NULL COMMENT '후기 FK',
file_name VARCHAR(200) NOT NULL COMMENT '파일명',
url VARCHAR(300) NOT NULL COMMENT '이미지 URL',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
) COMMENT '후기 이미지';

-- =============================================
-- 14. 후기 신고 테이블
-- =============================================
CREATE TABLE review_report (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '신고 고유번호',
review_id BIGINT NOT NULL COMMENT '후기 FK',
reporter_id BIGINT NOT NULL COMMENT '신고자 FK',
reason VARCHAR(200) COMMENT '신고 사유',
status VARCHAR(20) DEFAULT 'WAIT' COMMENT '처리 상태 (WAIT/PROCESSED/REJECTED)',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '신고일시',
processed_at TIMESTAMP COMMENT '처리일시',
FOREIGN KEY (review_id) REFERENCES review(id),
FOREIGN KEY (reporter_id) REFERENCES users(id)
) COMMENT '후기 신고';

-- =============================================
-- 15. 이벤트/축제 테이블
-- =============================================
CREATE TABLE event (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '이벤트 고유번호',
name VARCHAR(100) NOT NULL COMMENT '이벤트명',
description TEXT COMMENT '설명',
region_id BIGINT COMMENT '지역 FK',
type VARCHAR(30) COMMENT '유형 (축제/먹거리/시즌테마)',
start_date DATE COMMENT '시작일',
end_date DATE COMMENT '종료일',
priority INT DEFAULT 0 COMMENT '노출 우선순위',
is_main BOOLEAN DEFAULT FALSE COMMENT '메인 노출 여부',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
FOREIGN KEY (region_id) REFERENCES region(id)
) COMMENT '이벤트/축제';

-- =============================================
-- 16. 이벤트 이미지 테이블
-- =============================================
CREATE TABLE event_image (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '이미지 고유번호',
event_id BIGINT NOT NULL COMMENT '이벤트 FK',
file_name VARCHAR(200) NOT NULL COMMENT '파일명',
url VARCHAR(300) NOT NULL COMMENT '이미지 URL',
is_thumbnail BOOLEAN DEFAULT FALSE COMMENT '썸네일 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE
) COMMENT '이벤트 이미지';

-- =============================================
-- 17. 포인트 내역 테이블
-- =============================================
CREATE TABLE point (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '포인트 고유번호',
user_id BIGINT NOT NULL COMMENT '회원 FK',
amount INT NOT NULL COMMENT '포인트 금액',
type VARCHAR(20) NOT NULL COMMENT '유형 (적립/사용/이벤트/후기)',
description VARCHAR(200) COMMENT '설명',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '발생일시',
FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT '포인트 내역';

-- =============================================
-- 18. 메인 배너 테이블
-- =============================================
CREATE TABLE banner (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '배너 고유번호',
title VARCHAR(100) COMMENT '배너 제목',
description VARCHAR(200) COMMENT '배너 설명',
image_url VARCHAR(300) NOT NULL COMMENT '이미지 URL',
link_url VARCHAR(300) COMMENT '클릭 시 이동 URL',
position VARCHAR(20) DEFAULT 'MAIN' COMMENT '위치 (MAIN/SUB)',
priority INT DEFAULT 0 COMMENT '노출 우선순위',
start_date DATE COMMENT '노출 시작일',
end_date DATE COMMENT '노출 종료일',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT '메인 배너';

-- =============================================
-- 19. 추천 콘텐츠 / 인기 여행 테이블
-- =============================================
CREATE TABLE recommend_content (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '콘텐츠 고유번호',
title VARCHAR(100) NOT NULL COMMENT '콘텐츠 제목',
description TEXT COMMENT '설명',
type VARCHAR(30) COMMENT '유형 (인기여행/추천코스/시즌추천)',
target_id BIGINT COMMENT '연결 대상 ID (plan_id, event_id 등)',
target_type VARCHAR(20) COMMENT '연결 대상 유형 (PLAN/EVENT/SPOT)',
image_url VARCHAR(300) COMMENT '이미지 URL',
priority INT DEFAULT 0 COMMENT '노출 우선순위',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT '추천 콘텐츠/인기 여행';

-- =============================================
-- 20. 최근 본 계획 / 다시 보기 테이블
-- =============================================
CREATE TABLE view_history (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '히스토리 고유번호',
user_id BIGINT NOT NULL COMMENT '회원 FK',
plan_id BIGINT NOT NULL COMMENT '계획 FK',
viewed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '조회일시',
FOREIGN KEY (user_id) REFERENCES users(id),
FOREIGN KEY (plan_id) REFERENCES travel_plan(id)
) COMMENT '최근 본 계획/다시 보기';

-- =============================================
-- 21. 문의/FAQ 테이블
-- =============================================
CREATE TABLE inquiry (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '문의 고유번호',
user_id BIGINT COMMENT '회원 FK (비회원 문의 가능)',
type VARCHAR(20) COMMENT '유형 (문의/FAQ)',
category VARCHAR(30) COMMENT '카테고리 (결제/여행/기타 등)',
title VARCHAR(100) COMMENT '제목',
content TEXT COMMENT '내용',
answer TEXT COMMENT '답변',
status VARCHAR(20) DEFAULT 'WAIT' COMMENT '상태 (WAIT/ANSWERED)',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '문의일시',
answered_at TIMESTAMP COMMENT '답변일시',
FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT '문의/FAQ';

-- =============================================
-- 22. 관리자 규칙 테이블 (자동생성/랜덤/결제 등)
-- =============================================
CREATE TABLE rule (
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '규칙 고유번호',
type VARCHAR(30) NOT NULL COMMENT '규칙 유형 (일반/랜덤/이벤트/결제/포인트)',
name VARCHAR(100) COMMENT '규칙명',
description VARCHAR(200) COMMENT '규칙 설명',
value TEXT COMMENT '규칙 값 (JSON 또는 설정값)',
is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시'
) COMMENT '관리자 규칙';