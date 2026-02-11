package com.example.jpa.domain.ticket.repository;

import com.example.jpa.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByRegionIdAndIsActiveTrue(Long regionId);

    List<Ticket> findByRegionIdAndPriceLessThanEqualAndIsActiveTrue(Long regionId, Integer maxPrice);

    List<Ticket> findByRegionIdAndCategoryAndIsActiveTrue(Long regionId, String category);

    @Query("SELECT t FROM Ticket t WHERE t.regionId = :regionId " +
           "AND t.price <= :maxPrice " +
           "AND t.keywords LIKE %:keyword% " +
           "AND t.isActive = true")
    List<Ticket> findByRegionAndPriceAndKeyword(
            @Param("regionId") Long regionId,
            @Param("maxPrice") Integer maxPrice,
            @Param("keyword") String keyword);

    @Query("SELECT t FROM Ticket t WHERE t.regionId = :regionId " +
           "AND t.availableFrom <= :date AND t.availableTo >= :date " +
           "AND t.isActive = true")
    List<Ticket> findAvailableByRegionAndDate(
            @Param("regionId") Long regionId,
            @Param("date") LocalDate date);

    List<Ticket> findByKeywordsContainingAndIsActiveTrue(String keyword);
}
