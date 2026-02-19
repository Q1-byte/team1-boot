package com.example.jpa.domain.plan.repository;

import com.example.jpa.domain.plan.entity.PlanSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanSpotRepository extends JpaRepository<PlanSpot, Long> {

    List<PlanSpot> findByPlanIdOrderByDayAsc(Long planId);

    void deleteByPlanId(Long planId);
}
