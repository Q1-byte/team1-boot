-- =============================================
-- 여행 플랫폼 데이터베이스 초기화 스크립트
-- 작성일: 2026-02-13
-- 데이터베이스: teamdb
-- 총 테이블 수: 29개
-- =============================================

-- 데이터베이스 생성 및 선택 (필요시)
-- CREATE DATABASE IF NOT EXISTS teamdb DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- USE teamdb;

-- 기존 테이블 삭제 (역순)
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS content;
DROP TABLE IF EXISTS inquiry_file;
DROP TABLE IF EXISTS inquiry;
DROP TABLE IF EXISTS rule;
DROP TABLE IF EXISTS view_history;
DROP TABLE IF EXISTS recommend_content;
DROP TABLE IF EXISTS banner;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS accommodation;
DROP TABLE IF EXISTS review_comment;
DROP TABLE IF EXISTS review_report;
DROP TABLE IF EXISTS review_image;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS point;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS plan_detail;
DROP TABLE IF EXISTS travel_plan;
DROP TABLE IF EXISTS checklist;
DROP TABLE IF EXISTS plan_template;
DROP TABLE IF EXISTS spot_keyword;
DROP TABLE IF EXISTS spot_image;
DROP TABLE IF EXISTS travel_spot;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS inquiry_category;
DROP TABLE IF EXISTS keyword;
DROP TABLE IF EXISTS region;
DROP TABLE IF EXISTS users;

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
    active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시'
) COMMENT '키워드/태그 마스터';

-- =============================================
-- 4. 문의 카테고리 테이블 (inquiry보다 먼저 생성)
-- =============================================
CREATE TABLE inquiry_category (
    code VARCHAR(30) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    display_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 5. 이벤트/축제 테이블
-- =============================================
CREATE TABLE event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content_id BIGINT,
    name VARCHAR(255) NOT NULL,
    address TEXT,
    addr2 TEXT,
    zip_code VARCHAR(20),
    tel VARCHAR(255),
    image_url VARCHAR(500),
    description TEXT,
    cat1 VARCHAR(50),
    cat2 VARCHAR(50),
    cat3 VARCHAR(50),
    start_date VARCHAR(20),
    end_date VARCHAR(20),
    map_x DOUBLE,
    map_y DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 6. 여행지(장소) 테이블
-- =============================================
CREATE TABLE travel_spot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '여행지 고유번호',
    api_id VARCHAR(50) UNIQUE COMMENT '외부 API 제공 ID (중복 저장 방지용)',
    name VARCHAR(100) NOT NULL COMMENT '여행지명',
    region_id BIGINT COMMENT '지역 FK',
    address VARCHAR(255) COMMENT '주소',
    latitude DECIMAL(10, 7) COMMENT '위도',
    longitude DECIMAL(11, 7) COMMENT '경도',
    image_url TEXT COMMENT '이미지 URL',
    category VARCHAR(50) COMMENT '카테고리 (맛집/관광 등)',
    description TEXT COMMENT '설명',
    avg_price INT UNSIGNED DEFAULT 0 COMMENT '평균 비용',
    avg_time INT UNSIGNED DEFAULT 0 COMMENT '평균 소요 시간(분)',
    is_active BOOLEAN DEFAULT TRUE COMMENT '활성화 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',
    FOREIGN KEY (region_id) REFERENCES region(id)
) COMMENT '여행지(장소) 정보';

-- =============================================
-- 7. 여행지 이미지 테이블
-- =============================================
CREATE TABLE spot_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '이미지 고유번호',
    spot_id BIGINT NOT NULL COMMENT '여행지 FK',
    file_name VARCHAR(255) COMMENT '파일명 (서버 저장용)',
    url TEXT NOT NULL COMMENT '이미지 URL',
    cpyrht_type VARCHAR(50) COMMENT '저작권 유형 (예: Type1, Type3)',
    is_thumbnail BOOLEAN DEFAULT FALSE COMMENT '썸네일 여부',
    sort_order INT DEFAULT 0 COMMENT '출력 순서',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    FOREIGN KEY (spot_id) REFERENCES travel_spot(id) ON DELETE CASCADE
) COMMENT '여행지 이미지';

-- =============================================
-- 8. 여행지-키워드 매핑 테이블
-- =============================================
CREATE TABLE spot_keyword (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '매핑 고유번호',
    spot_id BIGINT NOT NULL COMMENT '여행지 FK',
    keyword_id BIGINT NOT NULL COMMENT '키워드 FK',
    FOREIGN KEY (spot_id) REFERENCES travel_spot(id) ON DELETE CASCADE,
    FOREIGN KEY (keyword_id) REFERENCES keyword(id) ON DELETE CASCADE
) COMMENT '여행지-키워드 매핑';

-- =============================================
-- 9. 일정 템플릿 테이블 (관리자용)
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
-- 11. 여행 계획 테이블 (일반/랜덤/이벤트)
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
-- 12. 여행 일정 상세 테이블
-- =============================================
CREATE TABLE plan_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '상세 고유번호',
    plan_id BIGINT NOT NULL COMMENT '계획 FK',
    spot_id BIGINT COMMENT '방문 장소 FK',
    checklist_id BIGINT COMMENT '체크리스트 항목 FK',
    day INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '일차 (1일차, 2일차...)',
    order_num INT UNSIGNED NOT NULL COMMENT '일정 순서',
    price INT UNSIGNED DEFAULT 0 COMMENT '예상 비용',
    start_time TIME COMMENT '시작 시간',
    end_time TIME COMMENT '종료 시간',
    is_selected BOOLEAN DEFAULT TRUE COMMENT '결제 포함 여부',
    FOREIGN KEY (plan_id) REFERENCES travel_plan(id) ON DELETE CASCADE,
    FOREIGN KEY (spot_id) REFERENCES travel_spot(id) ON DELETE SET NULL,
    FOREIGN KEY (checklist_id) REFERENCES checklist(id) ON DELETE SET NULL
) COMMENT '여행 일정 상세';

-- =============================================
-- 13. 결제 테이블
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
-- 14. 후기 테이블
-- =============================================
CREATE TABLE review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '리뷰 고유 번호',
    title VARCHAR(255) COMMENT '리뷰 제목',
    content TEXT NOT NULL COMMENT '리뷰 본문 (블로그 형식)',
    user_id BIGINT NOT NULL COMMENT '작성자 ID (FK)',
    plan_id BIGINT COMMENT '여행 일정 ID',
    rating INT COMMENT '평점',
    is_random TINYINT(1) DEFAULT 0 COMMENT '랜덤 여부',
    is_public TINYINT(1) DEFAULT 1 COMMENT '공개 여부',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '삭제 여부 (Soft Delete)',
    view_count INT DEFAULT 0 COMMENT '조회수',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '생성 일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    INDEX idx_review_created_at (created_at),
    INDEX idx_review_view_count (view_count),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 15. 후기 이미지 테이블
-- =============================================
CREATE TABLE review_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '이미지 고유 번호',
    review_id BIGINT COMMENT '리뷰 ID (FK)',
    origin_name VARCHAR(255) COMMENT '원본 파일명',
    stored_url VARCHAR(255) COMMENT '서버/S3 저장 경로 (URL)',
    sort_order INT COMMENT '이미지 정렬 순서',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '등록 일시',
    CONSTRAINT fk_image_review FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 16. 후기 신고 테이블
-- =============================================
CREATE TABLE review_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '신고 고유 번호',
    review_id BIGINT NOT NULL COMMENT '신고 대상 리뷰 ID (FK)',
    user_id BIGINT NOT NULL COMMENT '신고한 사람 ID (FK)',
    category VARCHAR(255) COMMENT '신고 카테고리',
    reason TEXT NOT NULL COMMENT '상세 신고 사유',
    status VARCHAR(50) DEFAULT 'PENDING' COMMENT '처리 상태',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '신고 일시',
    processed_at DATETIME COMMENT '처리 완료 일시',
    CONSTRAINT fk_report_review FOREIGN KEY (review_id) REFERENCES review(id),
    CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 17. 후기 댓글 테이블
-- =============================================
CREATE TABLE review_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 고유 번호',
    review_id BIGINT COMMENT '리뷰 ID (FK)',
    user_id BIGINT COMMENT '작성자 ID (FK)',
    parent_id BIGINT COMMENT '부모 댓글 ID (대댓글용)',
    content TEXT NOT NULL COMMENT '댓글 내용',
    is_deleted TINYINT(1) DEFAULT 0 COMMENT '삭제 여부',
    is_secret TINYINT(1) DEFAULT 0 COMMENT '비밀글 여부',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '작성 일시',
    update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 일시',
    CONSTRAINT fk_comment_review FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_id) REFERENCES review_comment(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =============================================
-- 18. 포인트 내역 테이블
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
-- 19. 메인 배너 테이블
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
-- 20. 추천 콘텐츠 / 인기 여행 테이블
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
-- 21. 최근 본 계획 / 다시 보기 테이블
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
-- 22. 문의 테이블
-- =============================================
CREATE TABLE inquiry (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    category VARCHAR(30) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    answer TEXT,
    status ENUM('WAIT','ANSWERED') NOT NULL DEFAULT 'WAIT',
    answered_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    answered_at TIMESTAMP,
    deleted_at TIMESTAMP NULL,
    is_deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category) REFERENCES inquiry_category(code),
    FOREIGN KEY (answered_by) REFERENCES users(id),
    INDEX idx_user_status (user_id, status),
    INDEX idx_created_at (created_at),
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_deleted (is_deleted, created_at)
);

-- =============================================
-- 23. 문의 파일 테이블
-- =============================================
CREATE TABLE inquiry_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    inquiry_id BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size INT UNSIGNED NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    download_count INT UNSIGNED DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (inquiry_id) REFERENCES inquiry(id) ON DELETE CASCADE,
    INDEX idx_inquiry_id (inquiry_id)
);

-- =============================================
-- 24. 관리자 규칙 테이블
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

-- =============================================
-- 25. 숙소 테이블
-- =============================================
CREATE TABLE accommodation (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255),
    image_url       VARCHAR(500),
    region_id       BIGINT,
    latitude        DOUBLE,
    longitude       DOUBLE,
    price_per_night INT,
    type            VARCHAR(30),
    keywords        VARCHAR(200),
    is_active       BIT          DEFAULT 1,
    created_at      DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- 26. 액티비티 테이블
-- =============================================
CREATE TABLE activity (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    name             VARCHAR(255) NOT NULL,
    description      VARCHAR(255),
    image_url        VARCHAR(500),
    region_id        BIGINT,
    latitude         DOUBLE,
    longitude        DOUBLE,
    price            INT,
    category         VARCHAR(30),
    keywords         VARCHAR(200),
    duration_minutes INT,
    is_active        BIT          DEFAULT 1,
    created_at       DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- 27. 티켓 테이블
-- =============================================
CREATE TABLE ticket (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    name           VARCHAR(255) NOT NULL,
    description    VARCHAR(255),
    image_url      VARCHAR(500),
    region_id      BIGINT,
    latitude       DOUBLE,
    longitude      DOUBLE,
    price          INT,
    category       VARCHAR(30),
    keywords       VARCHAR(200),
    available_from DATE,
    available_to   DATE,
    is_active      BIT          DEFAULT 1,
    created_at     DATETIME(6),
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- 28. 콘텐츠 테이블 (가이드/공지사항)
-- =============================================
CREATE TABLE content (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    category VARCHAR(50),
    view_count INT DEFAULT 0,
    reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 29. 히스토리 테이블 (시스템 로그)
-- =============================================
CREATE TABLE history (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    target_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- 초기 데이터 INSERT
-- =============================================

-- KEYWORD 데이터 (23건)
INSERT INTO keyword (name, category, active) VALUES
('스릴', '테마', TRUE), ('자연', '테마', TRUE), ('힐링', '테마', TRUE), ('트레킹', '테마', TRUE),
('데이트', '테마', TRUE), ('추억', '테마', TRUE), ('예술', '테마', TRUE), ('체험', '테마', TRUE),
('오션뷰', '환경', TRUE), ('역세권', '환경', TRUE), ('시티뷰', '환경', TRUE), ('숲세권', '환경', TRUE),
('호수뷰', '환경', TRUE), ('전통/역사', '환경', TRUE), ('시내중심', '환경', TRUE),
('가족친화', '조건', TRUE), ('가성비', '조건', TRUE), ('조용한', '조건', TRUE), ('반려동물동반', '조건', TRUE),
('비즈니스', '조건', TRUE), ('럭셔리', '조건', TRUE), ('루프탑/야외', '조건', TRUE), ('취사가능', '조건', TRUE);

-- =============================================
-- 지역별 UPDATE (region_id 매핑)
-- =============================================
-- region ID: 1=서울, 2=인천, 3=부산, 4=울산, 5=경기, 6=강원, 7=충북, 8=충남, 9=경북, 10=경남, 11=전북, 12=전남, 13=제주

-- 충남(8) 지역 데이터 업데이트
UPDATE accommodation SET region_id = 8 WHERE id BETWEEN 27 AND 29;
UPDATE activity SET region_id = 8 WHERE id BETWEEN 26 AND 28;
UPDATE ticket SET region_id = 8 WHERE id BETWEEN 19 AND 20;

-- =============================================
-- 테이블 생성 및 초기 데이터 완료 확인
-- =============================================
SELECT COUNT(*) as table_count FROM information_schema.tables WHERE table_schema = DATABASE();
SELECT 'keyword' AS tbl, COUNT(*) AS cnt FROM keyword
UNION ALL SELECT 'accommodation', COUNT(*) FROM accommodation
UNION ALL SELECT 'activity', COUNT(*) FROM activity
UNION ALL SELECT 'ticket', COUNT(*) FROM ticket
UNION ALL SELECT 'event', COUNT(*) FROM event;

SELECT '=== 데이터베이스 초기화 완료 ===' AS message;
