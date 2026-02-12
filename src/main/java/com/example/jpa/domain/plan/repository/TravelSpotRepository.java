package com.example.jpa.domain.plan.repository;

import com.example.jpa.domain.plan.entity.TravelSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TravelSpotRepository extends JpaRepository<TravelSpot, Long> {

    // 키워드와 카테고리를 동시에 만족하는 장소 찾기
    @Query("SELECT DISTINCT ts FROM TravelSpot ts " +
            "JOIN ts.spotKeywords sk " +
            "JOIN sk.keyword k " +
            "WHERE REPLACE(k.name, '#', '') IN :keywordNames " +
            "AND ts.category = :category")
    List<TravelSpot> findAllByKeywordsAndCategory(
            @Param("keywordNames") List<String> keywordNames,
            @Param("category") String category);

    // 지역 + 카테고리만으로 장소 찾기 (키워드 매칭 부족 시 fallback)
    @Query("SELECT ts FROM TravelSpot ts " +
            "WHERE ts.category = :category " +
            "AND ts.address LIKE %:region%")
    List<TravelSpot> findByCategoryAndRegion(
            @Param("category") String category,
            @Param("region") String region);
}