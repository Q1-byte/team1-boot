package com.example.jpa.domain.spot.repository;

import com.example.jpa.domain.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    // 1. 중복 수집 방지를 위한 핵심 메서드
    // 엔티티의 필드명이 apiId인 경우 정확히 이 이름을 써야 합니다.
    boolean existsByApiId(String apiId);

    // 2. (선택사항) 나중에 카테고리별로 필터링해서 장소를 가져올 때 사용
    List<Spot> findByCategory(String category);

    // 3. (선택사항) 이름으로 장소 검색할 때 사용
    List<Spot> findByNameContaining(String name);
}

