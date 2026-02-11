package com.example.jpa.domain.plan.controller;

import com.example.jpa.domain.plan.dto.PlanDto;
import com.example.jpa.domain.plan.entity.Plan;
import com.example.jpa.domain.plan.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/plans")
public class AdminPlanController {
    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<Plan>> list() {
        return ResponseEntity.ok(planService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> detail(@PathVariable Long id) {
        return ResponseEntity.ok(planService.findOne(id));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PlanDto.Request request) {
        Plan plan = new Plan();
        plan.setName(request.getName());
        plan.setPrice(request.getPrice());
        plan.setDescription(request.getDescription());
        return ResponseEntity.ok(planService.savePlan(plan));
    }
}