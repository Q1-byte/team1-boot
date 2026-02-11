package com.example.jpa.domain.content.controller;

import com.example.jpa.domain.content.dto.ContentDto;
import com.example.jpa.domain.content.entity.Content;
import com.example.jpa.domain.content.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/contents")
@RequiredArgsConstructor
public class AdminContentController {

    private final ContentService contentService;

    @GetMapping
    public ResponseEntity<Page<Content>> getList(Pageable pageable) {
        return ResponseEntity.ok(contentService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody ContentDto dto) {
        contentService.insert(dto);
        return ResponseEntity.ok("등록 성공");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modify(@PathVariable int id, @RequestBody ContentDto dto) {
        contentService.update(id, dto);
        return ResponseEntity.ok("수정 성공");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable int id) {
        contentService.delete(id);
        return ResponseEntity.ok("삭제 성공");
    }
}