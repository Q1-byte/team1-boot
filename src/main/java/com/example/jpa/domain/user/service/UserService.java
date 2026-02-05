package com.example.jpa.domain.user.service;

import com.example.jpa.domain.user.dto.LoginRequestDto;
import com.example.jpa.domain.user.dto.SignupRequestDto;
import com.example.jpa.domain.user.dto.UserResponseDto;
import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * 회원가입
     */
    @Transactional
    public UserResponseDto signup(SignupRequestDto requestDto) {
        // 아이디 중복 체크
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        
        // 이메일 중복 체크
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        
        // 비밀번호 확인
        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        // 비밀번호 암호화 (실제로는 BCryptPasswordEncoder 등 사용)
        String encodedPassword = encodePassword(requestDto.getPassword());
        
        // 사용자 저장
        User user = requestDto.toEntity(encodedPassword);
        User savedUser = userRepository.save(user);
        
        log.info("회원가입 완료: {}", savedUser.getUsername());
        
        return UserResponseDto.fromEntity(savedUser);
    }
    
    /**
     * 로그인
     */
    public UserResponseDto login(LoginRequestDto requestDto) {
        // 아이디로 사용자 조회
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
        
        // 비밀번호 검증
        String encodedPassword = encodePassword(requestDto.getPassword());
        if (!user.getPassword().equals(encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        log.info("로그인 성공: {}", user.getUsername());
        
        return UserResponseDto.fromEntity(user);
    }
    
    /**
     * 아이디 중복 체크
     */
    public boolean checkUsernameDuplicate(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * 이메일 중복 체크
     */
    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * 사용자 ID로 조회
     */
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
        return UserResponseDto.fromEntity(user);
    }
    
    /**
     * 사용자 정보 수정
     */
    @Transactional
    public UserResponseDto updateUser(Long id, String phone, String keywordPref) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
        
        if (phone != null) {
            user.setPhone(phone);
        }
        if (keywordPref != null) {
            user.setKeywordPref(keywordPref);
        }
        
        return UserResponseDto.fromEntity(user);
    }
    
    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
        
        String encodedCurrentPassword = encodePassword(currentPassword);
        if (!user.getPassword().equals(encodedCurrentPassword)) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }
        
        user.setPassword(encodePassword(newPassword));
        log.info("비밀번호 변경 완료: {}", user.getUsername());
    }
    
    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
        
        userRepository.delete(user);
        log.info("회원 탈퇴 완료: {}", user.getUsername());
    }
    
    /**
     * 비밀번호 암호화 (간단한 구현 - 실제로는 BCryptPasswordEncoder 사용 권장)
     */
    private String encodePassword(String password) {
        // TODO: 실제 운영 시 BCryptPasswordEncoder로 교체
        // 현재는 간단히 해시값 사용
        return String.valueOf(password.hashCode());
    }

    // ==================== 관리자용 메서드 ====================

    /**
     * 전체 회원 목록 조회 (페이징)
     */
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 회원 상태 변경
     */
    @Transactional
    public void updateStatus(Long id, String status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 존재하지 않습니다."));
        user.setStatus(status);
    }

    /**
     * 회원 저장 (관리자용)
     */
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}
