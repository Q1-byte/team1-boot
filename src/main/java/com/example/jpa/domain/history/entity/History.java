package com.example.jpa.domain.history.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 컨트롤러에서 new History()를 쓰려면 접근 제한이 없어야 해요!
@EntityListeners(AuditingEntityListener.class) // 생성 시간을 자동으로 기록하기 위함
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String type;      // 히스토리 종류 (예: "PAYMENT", "LOGIN", "SYSTEM")

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;   // 히스토리 상세 내용 (설명)

    private Integer targetId; // 관련된 데이터의 ID (예: 결제 번호, 사용자 번호 등)

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 기록된 시간 (자동 생성)
}