package com.example.jpa.domain.event.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // API 데이터는 고유 ID를 가지지만, 목데이터는 null이므로 유니크 제약만 걸고 null을 허용합니다.
    @Column(unique = true, nullable = true)
    private Long contentId;

    private String name; // title (행사명)
    private String address; // addr1 (주소)
    private String addr2; // addr2 (상세주소)
    private String zipCode; // zipcode (우편번호)
    private String tel; // tel (전화번호)
    @Column(length = 500)
    private String imageUrl; // firstimage (이미지 경로)

    private String description; // [추가] 축제 상세 설명 또는 자동 생성 문구

    private String cat1; // 서비스 분류 1
    private String cat2; // 서비스 분류 2 (카테고리 필터용)
    private String cat3; // 서비스 분류 3

    private String startDate; // eventstartdate (시작일)
    private String endDate; // eventenddate (종료일)

    private Double mapX; // mapx (경도)
    private Double mapY; // mapy (위도)
}