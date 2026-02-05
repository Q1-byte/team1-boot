package com.example.jpa.domain.history.controller;

import com.example.jpa.domain.history.entity.History;
import com.example.jpa.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/histories")
public class AdminHistoryController {
    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<History>> list() {
        return ResponseEntity.ok(historyService.findAll());
    }

    @PostMapping
    public ResponseEntity<Integer> create(@RequestBody History history) {
        // 이제 서비스가 Integer를 반환하므로 ok() 안에 넣을 수 있습니다.
        Integer savedId = historyService.saveHistory(history);
        return ResponseEntity.ok(savedId);
    }
}