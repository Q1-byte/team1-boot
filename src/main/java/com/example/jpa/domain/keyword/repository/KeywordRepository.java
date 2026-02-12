package com.example.jpa.domain.keyword.repository;

import com.example.jpa.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {
    java.util.Optional<Keyword> findByName(String name);

    @Query("SELECT COUNT(sk) FROM SpotKeyword sk")
    long countMappedSpotKeywords();
}