package com.example.jpa.domain.user.controller;

import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.save(user);
            return ResponseEntity.ok("회원 등록 성공!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("에러 발생: " + e.getMessage());
        }
    }

    // 목록 조회 (페이징 적용)
    @GetMapping
    public ResponseEntity<Page<User>> list(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    // 특정 회원 조회
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
        userService.deleteUser(id);
        return ResponseEntity.ok("Success");
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
