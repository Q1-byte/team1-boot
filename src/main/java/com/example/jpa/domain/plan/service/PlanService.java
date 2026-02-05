package com.example.jpa.domain.plan.service;

import com.example.jpa.domain.plan.entity.Plan;
import com.example.jpa.domain.plan.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanService {
    private final PlanRepository planRepository;

    @Transactional
    public Long savePlan(Plan plan) {
        return planRepository.save(plan).getId();
    }

    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    public Plan findOne(Long id) {
        return planRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void delete(Long id) {
        planRepository.deleteById(id);
    }
}