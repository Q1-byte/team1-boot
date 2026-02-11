package com.example.jpa.domain.activity.repository;

import com.example.jpa.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByRegionIdAndIsActiveTrue(Long regionId);

    List<Activity> findByRegionIdAndPriceLessThanEqualAndIsActiveTrue(Long regionId, Integer maxPrice);

    List<Activity> findByRegionIdAndCategoryAndIsActiveTrue(Long regionId, String category);

    @Query("SELECT a FROM Activity a WHERE a.regionId = :regionId " +
           "AND a.price <= :maxPrice " +
           "AND a.keywords LIKE %:keyword% " +
           "AND a.isActive = true")
    List<Activity> findByRegionAndPriceAndKeyword(
            @Param("regionId") Long regionId,
            @Param("maxPrice") Integer maxPrice,
            @Param("keyword") String keyword);

    List<Activity> findByKeywordsContainingAndIsActiveTrue(String keyword);
}
