package com.example.jpa.domain.keyword.controller;

import com.example.jpa.domain.keyword.entity.Keyword;
import com.example.jpa.domain.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/keywords")
public class AdminKeywordController {
    private final KeywordService keywordService;

    @GetMapping
    public ResponseEntity<List<Keyword>> list() {
        return ResponseEntity.ok(keywordService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody Keyword keyword) {
        return ResponseEntity.ok(keywordService.saveKeyword(keyword));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        keywordService.deleteKeyword(id);
        return ResponseEntity.ok().build();
    }
}