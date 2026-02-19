package com.example.jpa.domain.spot.repository;

import com.example.jpa.domain.spot.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    // ✅ 가챠 핵심: 특정 레벨 중 랜덤 1개 추출
    @Query("SELECT s FROM Spot s LEFT JOIN FETCH s.region WHERE s.level = :level ORDER BY function('RAND') LIMIT 1")
    Optional<Spot> findRandomSpotByLevel(@Param("level") Integer level);

    boolean existsByApiId(String apiId);

    // 어드민 검색 + 필터 (keyword, isActive 모두 optional)
    @Query("SELECT s FROM Spot s WHERE " +
           "(:keyword IS NULL OR s.name LIKE %:keyword% OR s.address LIKE %:keyword%) AND " +
           "(:isActive IS NULL OR s.isActive = :isActive)")
    Page<Spot> searchSpots(@Param("keyword") String keyword,
                           @Param("isActive") Boolean isActive,
                           Pageable pageable);
}