package com.example.jpa.domain.user.controller;

import com.example.jpa.domain.user.dto.UserResponseDto;
import com.example.jpa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    // 목록 조회 (페이지네이션 + keyword 검색 + role 필터)
    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(keyword, role, pageable));
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("해당 회원을 찾을 수 없습니다.");
        }
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("해당 회원을 찾을 수 없습니다.");
        }
    }

    // 포인트 수정
    @PatchMapping("/{id}/point")
    public ResponseEntity<?> updatePoint(@PathVariable Long id, @RequestBody Map<String, Integer> request) {
        try {
            Integer point = request.get("point");
            return ResponseEntity.ok(userService.updatePoint(id, point));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("해당 회원을 찾을 수 없습니다.");
        }
    }

    // 상태 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String newStatus = request.get("status");
            userService.updateStatus(id, newStatus);
            return ResponseEntity.ok("상태 변경 성공: " + newStatus);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("해당 회원을 찾을 수 없습니다.");
        }
    }
}
