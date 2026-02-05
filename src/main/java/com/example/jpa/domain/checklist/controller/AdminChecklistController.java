package com.example.jpa.domain.checklist.controller;

import com.example.jpa.domain.checklist.dto.ChecklistDto;
import com.example.jpa.domain.checklist.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/checklists")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminChecklistController {

    private final ChecklistService checklistService;

    @GetMapping
    public ResponseEntity<List<ChecklistDto>> getList() {
        return ResponseEntity.ok(checklistService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody ChecklistDto dto) {
        checklistService.insert(dto);
        return ResponseEntity.ok("등록 성공");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modify(@PathVariable int id, @RequestBody ChecklistDto dto) {
        checklistService.update(id, dto);
        return ResponseEntity.ok("수정 성공");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable int id) {
        checklistService.delete(id);
        return ResponseEntity.ok("삭제 성공");
    }
}