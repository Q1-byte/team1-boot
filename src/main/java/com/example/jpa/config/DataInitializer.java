package com.example.jpa.config;

import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        // 이미 데이터가 있으면 스킵
        if (userRepository.count() > 0) {
            log.info("초기 데이터가 이미 존재합니다.");
            return;
        }

        // 테스트 계정 생성
        User admin = User.builder()
                .username("admin")
                .password(String.valueOf("admin123".hashCode()))
                .email("admin@trip.com")
                .phone("010-1234-5678")
                .role("ADMIN")
                .keywordPref("힐링,맛집,자연")
                .point(5000)
                .build();

        User user1 = User.builder()
                .username("user1")
                .password(String.valueOf("user123".hashCode()))
                .email("user1@trip.com")
                .phone("010-9876-5432")
                .role("USER")
                .keywordPref("액티비티,체험,축제")
                .point(1000)
                .build();

        User test = User.builder()
                .username("test")
                .password(String.valueOf("test123".hashCode()))
                .email("test@trip.com")
                .phone("010-1111-2222")
                .role("USER")
                .keywordPref("맛집,카페")
                .point(500)
                .build();

        userRepository.save(admin);
        userRepository.save(user1);
        userRepository.save(test);

        log.info("테스트 계정 생성 완료!");
        log.info("- admin / admin123 (관리자)");
        log.info("- user1 / user123 (일반 사용자)");
        log.info("- test / test123 (일반 사용자)");
    }
}
