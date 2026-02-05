package com.example.jpa.domain.rule.controller;

import com.example.jpa.domain.rule.dto.RuleDto;
import com.example.jpa.domain.rule.entity.Rule;
import com.example.jpa.domain.rule.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/rules")
public class AdminRuleController {
    private final RuleService ruleService;

    @GetMapping
    public ResponseEntity<List<Rule>> list() {
        return ResponseEntity.ok(ruleService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody RuleDto.Request request) {
        Rule rule = new Rule();
        rule.setTitle(request.getTitle());
        rule.setContent(request.getContent());
        return ResponseEntity.ok(ruleService.saveRule(rule));
    }
}