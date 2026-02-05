package com.example.jpa.domain.point.repository;

import com.example.jpa.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Integer> {
    // 특정 사용자의 포인트 내역을 최신순으로 조회
    List<Point> findByUserIdOrderByCreatedAtDesc(Long userId);
}