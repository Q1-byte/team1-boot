package com.example.jpa.domain.accommodation.repository;

import com.example.jpa.domain.accommodation.entity.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    List<Accommodation> findByRegionIdAndIsActiveTrue(Long regionId);

    List<Accommodation> findByRegionIdAndPricePerNightBetweenAndIsActiveTrue(
            Long regionId, Integer minPrice, Integer maxPrice);

    List<Accommodation> findByRegionIdAndTypeAndIsActiveTrue(Long regionId, String type);

    @Query("SELECT a FROM Accommodation a WHERE a.regionId = :regionId " +
           "AND a.pricePerNight BETWEEN :minPrice AND :maxPrice " +
           "AND a.keywords LIKE %:keyword% " +
           "AND a.isActive = true")
    List<Accommodation> findByRegionAndPriceAndKeyword(
            @Param("regionId") Long regionId,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("keyword") String keyword);

    List<Accommodation> findByKeywordsContainingAndIsActiveTrue(String keyword);
}
