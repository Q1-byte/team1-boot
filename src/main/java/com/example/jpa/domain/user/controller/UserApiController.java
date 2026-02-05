package com.example.jpa.domain.user.controller;

import com.example.jpa.common.response.ApiResponse;
import com.example.jpa.domain.user.dto.LoginRequestDto;
import com.example.jpa.domain.user.dto.SignupRequestDto;
import com.example.jpa.domain.user.dto.UserResponseDto;
import com.example.jpa.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserApiController {
    
    private final UserService userService;
    
    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserResponseDto>> signup(@RequestBody SignupRequestDto requestDto) {
        try {
            UserResponseDto user = userService.signup(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("회원가입이 완료되었습니다.", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    /**
     * 로그인 API
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponseDto>> login(@RequestBody LoginRequestDto requestDto, HttpSession session) {
        try {
            UserResponseDto user = userService.login(requestDto);
            // 세션에 사용자 정보 저장
            session.setAttribute("loginUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            log.info("API 로그인 성공 - 세션 저장: {}", user.getUsername());
            return ResponseEntity.ok(ApiResponse.success("로그인 성공", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.unauthorized(e.getMessage()));
        }
    }
    
    /**
     * 아이디 중복 체크 API
     */
    @GetMapping("/check-username")
    public ResponseEntity<ApiResponse<Boolean>> checkUsername(@RequestParam String username) {
        boolean isDuplicate = userService.checkUsernameDuplicate(username);
        String message = isDuplicate ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다.";
        return ResponseEntity.ok(ApiResponse.success(message, isDuplicate));
    }
    
    /**
     * 이메일 중복 체크 API
     */
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(@RequestParam String email) {
        boolean isDuplicate = userService.checkEmailDuplicate(email);
        String message = isDuplicate ? "이미 사용 중인 이메일입니다." : "사용 가능한 이메일입니다.";
        return ResponseEntity.ok(ApiResponse.success(message, isDuplicate));
    }
    
    /**
     * 사용자 정보 조회 API
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUser(@PathVariable Long id) {
        try {
            UserResponseDto user = userService.findById(id);
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.notFound(e.getMessage()));
        }
    }
    
    /**
     * 사용자 정보 수정 API
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String keywordPref) {
        try {
            UserResponseDto user = userService.updateUser(id, phone, keywordPref);
            return ResponseEntity.ok(ApiResponse.success("회원정보가 수정되었습니다.", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    /**
     * 비밀번호 변경 API
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long id,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        try {
            userService.changePassword(id, currentPassword, newPassword);
            return ResponseEntity.ok(ApiResponse.success("비밀번호가 변경되었습니다."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }
    
    /**
     * 회원 탈퇴 API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("회원 탈퇴가 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.badRequest(e.getMessage()));
        }
    }
}
