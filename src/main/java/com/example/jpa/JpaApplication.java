package com.example.jpa;

import com.example.jpa.domain.keyword.service.KeywordBatchService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }

    // 서버가 시작될 때 자동으로 배치를 실행하는 코드입니다.
    @Bean
    public CommandLineRunner runBatch(KeywordBatchService service) {
        return args -> {
            System.out.println("🚀 [시스템] 서버 시작과 동시에 키워드 배치를 실행합니다...");
            service.runInitialCategorization();
        };
    }
}