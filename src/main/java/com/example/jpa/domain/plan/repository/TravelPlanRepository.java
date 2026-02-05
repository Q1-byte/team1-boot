package com.example.jpa.domain.plan.repository;

import com.example.jpa.domain.plan.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelPlanRepository extends JpaRepository<TravelPlan, Long> {

    List<TravelPlan> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<TravelPlan> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status);
}
