package com.example.jpa.domain.history.repository;

import com.example.jpa.domain.history.entity.ViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViewHistoryRepository extends JpaRepository<ViewHistory, Long> {

    List<ViewHistory> findTop3ByUserIdOrderByViewedAtDesc(Long userId);
}
