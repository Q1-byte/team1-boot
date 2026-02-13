package com.example.jpa.domain.event.repository;

import com.example.jpa.domain.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // 이름으로 검색 + 페이징 (LIKE 검색)
    Page<Event> findByNameContaining(String name, Pageable pageable);

    // 공공데이터 cat2 코드로 필터링 + 페이징
    Page<Event> findByCat2(String cat2, Pageable pageable);

    // 이름 검색 + 카테고리 필터링 동시 적용
    Page<Event> findByNameContainingAndCat2(String name, String cat2, Pageable pageable);

    // 전체 조회 + 페이징 (기본 제공)
    Page<Event> findAll(Pageable pageable);

    Optional<Event> findByContentId(Long contentId);
}