package com.example.jpa.domain.user.repository;

import com.example.jpa.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // 닉네임 중복 체크
    boolean existsByNickname(String nickname);

    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 아이디와 비밀번호로 사용자 조회 (로그인용)
    Optional<User> findByUsernameAndPassword(String username, String password);

    // 어드민 검색 + role 필터 (모두 optional)
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR u.nickname LIKE %:keyword%) AND " +
           "(:role IS NULL OR u.role = :role)")
    Page<User> searchUsers(@Param("keyword") String keyword,
                           @Param("role") String role,
                           Pageable pageable);
}
