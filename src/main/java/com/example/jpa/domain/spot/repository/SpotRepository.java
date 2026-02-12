package com.example.jpa.domain.spot.repository;

import com.example.jpa.domain.spot.entity.Spot;
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
}