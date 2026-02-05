package com.example.jpa.domain.inquiry.entity;


import com.example.jpa.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "inquiry")
public class Inquiry {

    @Id // @Id가 빠져있다면 반드시 추가해야 합니다!
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false) // 초기값 설정을 위해 @Builder.Default 등을 고려해보세요.
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime regDate;

    // 2. 아래에 있던 기존 'private Member member' 부분은 통째로 삭제하세요!

    public void updateAnswer(String answer) {
        this.answer = answer;
        this.status = "COMPLETED";
    }
}