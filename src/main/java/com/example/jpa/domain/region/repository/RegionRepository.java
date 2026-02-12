package com.example.jpa.domain.region.repository;

import com.example.jpa.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByAreaCode(String areaCode);

    // 부모 지역(시/도) 목록
    List<Region> findByParentIsNullOrderByIdAsc();

    // 특정 부모의 자식 지역(시/군/구) 목록
    List<Region> findByParentIdAndIsActiveTrueOrderByIdAsc(Long parentId);
}