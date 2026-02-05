package com.example.jpa.config;

import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 어드민 계정이 없으면 생성
        if (!userRepository.existsByUsername("admin1")) {
            User admin = User.builder()
                    .username("admin1")
                    .password(passwordEncoder.encode("admin1234"))
                    .email("admin1@trip.com")
                    .phone("010-1234-5678")
                    .role("ADMIN")
                    .keywordPref("힐링,맛집,자연")
                    .point(5000)
                    .build();
            userRepository.save(admin);
            log.info("어드민 계정 생성 완료! (admin1 / admin1234)");
        }

        // 테스트 유저 계정이 없으면 생성
        if (!userRepository.existsByUsername("user1")) {
            User user1 = User.builder()
                    .username("user1")
                    .password(passwordEncoder.encode("user1234"))
                    .email("user1@trip.com")
                    .phone("010-9876-5432")
                    .role("USER")
                    .keywordPref("액티비티,체험,축제")
                    .point(1000)
                    .build();
            userRepository.save(user1);
            log.info("테스트 유저 계정 생성 완료! (user1 / user1234)");
        }

        if (!userRepository.existsByUsername("test")) {
            User test = User.builder()
                    .username("test")
                    .password(passwordEncoder.encode("test1234"))
                    .email("test@trip.com")
                    .phone("010-1111-2222")
                    .role("USER")
                    .keywordPref("맛집,카페")
                    .point(500)
                    .build();
            userRepository.save(test);
            log.info("테스트 유저 계정 생성 완료! (test / test1234)");
        }
    }
}
