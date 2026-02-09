package com.example.jpa.config; // [검증] 이미지상의 config 폴더 주소

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // 팀 공통 엔진에 영향을 주지 않고 리뷰 쪽 감사(Auditing) 기능을 활성화합니다.
public class ReviewJpaConfig {

}