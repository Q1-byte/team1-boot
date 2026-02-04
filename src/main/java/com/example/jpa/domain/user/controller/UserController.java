package com.example.jpa.domain.user.controller;

import com.example.jpa.domain.user.dto.LoginRequestDto;
import com.example.jpa.domain.user.dto.SignupRequestDto;
import com.example.jpa.domain.user.dto.UserResponseDto;
import com.example.jpa.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    
    private final UserService userService;
    
    /**
     * 회원가입 페이지
     */
    @GetMapping("/signup")
    public String signupForm() {
        return "user/signup";
    }
    
    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public String signup(SignupRequestDto requestDto, RedirectAttributes redirectAttributes) {
        try {
            userService.signup(requestDto);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "redirect:/user/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/signup";
        }
    }
    
    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }
    
    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(LoginRequestDto requestDto, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            UserResponseDto user = userService.login(requestDto);
            session.setAttribute("loginUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());
            
            log.info("로그인 성공 - 세션 저장: {}", user.getUsername());
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/login";
        }
    }
    
    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    /**
     * 마이페이지
     */
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        UserResponseDto user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user/mypage";
    }
    
    /**
     * 회원정보 수정 페이지
     */
    @GetMapping("/edit")
    public String editForm(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        UserResponseDto user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user/edit";
    }
    
    /**
     * 회원정보 수정 처리
     */
    @PostMapping("/edit")
    public String edit(@RequestParam String phone, 
                      @RequestParam String keywordPref,
                      HttpSession session, 
                      RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        try {
            userService.updateUser(userId, phone, keywordPref);
            redirectAttributes.addFlashAttribute("message", "회원정보가 수정되었습니다.");
            return "redirect:/user/mypage";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/edit";
        }
    }
    
    /**
     * 비밀번호 변경 페이지
     */
    @GetMapping("/change-password")
    public String changePasswordForm(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        return "user/change-password";
    }
    
    /**
     * 비밀번호 변경 처리
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                @RequestParam String newPassword,
                                @RequestParam String newPasswordConfirm,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        if (!newPassword.equals(newPasswordConfirm)) {
            redirectAttributes.addFlashAttribute("error", "새 비밀번호가 일치하지 않습니다.");
            return "redirect:/user/change-password";
        }
        
        try {
            userService.changePassword(userId, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("message", "비밀번호가 변경되었습니다.");
            return "redirect:/user/mypage";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/change-password";
        }
    }
    
    /**
     * 회원 탈퇴
     */
    @PostMapping("/delete")
    public String delete(HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        try {
            userService.deleteUser(userId);
            session.invalidate();
            redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/mypage";
        }
    }
}
