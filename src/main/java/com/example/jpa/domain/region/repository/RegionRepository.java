package com.example.jpa.domain.region.repository;

import com.example.jpa.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByAreaCode(String areaCode);
}