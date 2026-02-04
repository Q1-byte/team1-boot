package com.example.jpa.domain.user.repository;

import com.example.jpa.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 아이디로 사용자 조회
    Optional<User> findByUsername(String username);
    
    // 이메일로 사용자 조회
    Optional<User> findByEmail(String email);
    
    // 아이디 중복 체크
    boolean existsByUsername(String username);
    
    // 이메일 중복 체크
    boolean existsByEmail(String email);
    
    // 아이디와 비밀번호로 사용자 조회 (로그인용)
    Optional<User> findByUsernameAndPassword(String username, String password);
}
