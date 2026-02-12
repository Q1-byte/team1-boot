package com.example.jpa.domain.gacha.controller;

import com.example.jpa.domain.gacha.dto.GachaResponseDto;
import com.example.jpa.domain.gacha.service.GachaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gacha")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class GachaController {

    private final GachaService gachaService;

    // 프론트엔드 호출 예시: /api/gacha/draw?level=2
    @GetMapping("/draw")
    public ResponseEntity<GachaResponseDto> drawGacha(
            @RequestParam(value = "level", required = false) Integer level) { // required = false로 테스트

        System.out.println(">>> 컨트롤러 도달 확인! 파라미터 level: " + level);

        if (level == null) {
            System.out.println("❌ level 파라미터가 전달되지 않았습니다!");
            return ResponseEntity.badRequest().build();
        }

        GachaResponseDto response = gachaService.drawGacha(level);
        return ResponseEntity.ok(response);
    }
}