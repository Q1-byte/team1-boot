package com.example.jpa.domain.user.controller;

import com.example.jpa.domain.user.entity.Member;
import com.example.jpa.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // 리액트 서버 허용 (CORS 해결)
public class AdminUserController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        try {
            // member 객체 안에 username, password, address 등이 이미 담겨 있습니다.
            memberService.insert(member);
            return ResponseEntity.ok("멤버 등록 성공!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("에러 발생: " + e.getMessage());
        }
    }
    // 목록 조회 (페이징 적용)
    @GetMapping
    public ResponseEntity<Page<Member>> list(Pageable pageable) {
        return ResponseEntity.ok(memberService.findByAll(pageable));
    }

    // 특정 회원 조회
    @GetMapping("/{id}")
    public ResponseEntity<Member> getOne(@PathVariable int id) {
        return ResponseEntity.ok(memberService.findById(id));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable int id) {
        memberService.delete(id);
        return ResponseEntity.ok("Success");
    }

    // AdminUserController.java 안에 추가
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            Member member = memberService.findById(id);
            String newStatus = request.get("status");

            member.setStatus(newStatus); // 상태 변경
            memberService.update(member); // 수정 메서드 호출 (기존에 만든 update 사용)

            return ResponseEntity.ok("상태 변경 성공: " + newStatus);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("해당 회원을 찾을 수 없습니다.");
        }
    }
}
