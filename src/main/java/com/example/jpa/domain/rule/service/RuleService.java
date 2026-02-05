package com.example.jpa.domain.rule.service;

import com.example.jpa.domain.rule.entity.Rule;
import com.example.jpa.domain.rule.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RuleService {
    private final RuleRepository ruleRepository;

    @Transactional
    public Long saveRule(Rule rule) {
        return ruleRepository.save(rule).getId();
    }

    public List<Rule> findAll() {
        return ruleRepository.findAll();
    }
}