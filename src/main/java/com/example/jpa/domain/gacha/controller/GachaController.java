package com.example.jpa.domain.gacha.controller;

import com.example.jpa.domain.gacha.dto.GachaResponseDto;
import com.example.jpa.domain.gacha.service.GachaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gacha")
@CrossOrigin(origins = {"http://localhost:5173", "http://43.201.117.124"})
@RequiredArgsConstructor
public class GachaController {

    private final GachaService gachaService;

    // 프론트엔드 호출 예시: /api/gacha/draw?level=2
    @GetMapping("/draw")
    public ResponseEntity<GachaResponseDto> drawGacha(
            @RequestParam(value = "level", required = false) Integer level) { // required = false로 테스트

        if (level == null) {
            return ResponseEntity.badRequest().build();
        }

        GachaResponseDto response = gachaService.drawGacha(level);
        return ResponseEntity.ok(response);
    }
}