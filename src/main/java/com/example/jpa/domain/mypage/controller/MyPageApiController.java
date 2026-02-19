package com.example.jpa.domain.mypage.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.mypage.dto.MyPageResponseDto;
import com.example.jpa.domain.mypage.dto.PasswordChangeRequestDto;
import com.example.jpa.domain.mypage.dto.ProfileUpdateRequestDto;
import com.example.jpa.domain.mypage.service.MyPageService;
import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import com.example.jpa.domain.point.dto.PointResponseDto;
import com.example.jpa.domain.user.dto.UserResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageApiController {

    private final MyPageService myPageService;

    /**
     * 마이페이지 메인 조회 (회원정보 + 최근 본 계획 3개)
     */
    @GetMapping({"", "/main"})
    public ResponseEntity<ApiResponse<MyPageResponseDto>> getMyPage(HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        try {
            MyPageResponseDto myPage = myPageService.getMyPageMain(userId);
            return ResponseEntity.ok(ApiResponse.success(myPage));
        } catch (Exception e) {
            log.error("마이페이지 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }

    /**
     * 내 여행 계획 예약 내역 조회
     */
    @GetMapping("/plans")
    public ResponseEntity<ApiResponse<List<TravelPlanResponseDto>>> getMyPlans(HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        try {
            List<TravelPlanResponseDto> plans = myPageService.getMyPlans(userId);
            return ResponseEntity.ok(ApiResponse.success(plans));
        } catch (Exception e) {
            log.error("여행 계획 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }

    /**
     * 내 포인트 내역 조회
     */
    @GetMapping("/points")
    public ResponseEntity<ApiResponse<List<PointResponseDto>>> getMyPoints(HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        try {
            List<PointResponseDto> points = myPageService.getMyPoints(userId);
            return ResponseEntity.ok(ApiResponse.success(points));
        } catch (Exception e) {
            log.error("포인트 내역 조회 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }

    /**
     * 프로필 수정 (연락처, 여행성향)
     */
    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateProfile(
            @RequestBody ProfileUpdateRequestDto requestDto,
            HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        try {
            UserResponseDto user = myPageService.updateProfile(userId, requestDto);
            return ResponseEntity.ok(ApiResponse.success("프로필이 수정되었습니다.", user));
        } catch (Exception e) {
            log.error("프로필 수정 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody PasswordChangeRequestDto requestDto,
            HttpSession session) {
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized("로그인이 필요합니다."));
        }

        try {
            myPageService.changePassword(userId, requestDto);
            return ResponseEntity.ok(ApiResponse.success("비밀번호가 변경되었습니다."));
        } catch (IllegalArgumentException e) {
            log.error("비밀번호 변경 실패: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }

    /**
     * 세션에서 userId 가져오기
     */
    private Long getUserIdFromSession(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }
}
