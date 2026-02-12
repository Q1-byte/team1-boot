package com.example.jpa.domain.gacha.repository;

import com.example.jpa.domain.region.entity.Region;
import com.example.jpa.domain.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GachaRepository extends JpaRepository<Spot, Long> {

    // 💡 레벨에 맞는 데이터 중 무작위로 1개만 추출
    @Query(value = "SELECT * FROM travel_spot WHERE level = :level ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Spot> findRandomSpotByLevel(@Param("level") int level);
}