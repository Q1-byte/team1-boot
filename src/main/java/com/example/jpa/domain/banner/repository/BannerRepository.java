package com.example.jpa.domain.banner.repository;

import com.example.jpa.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Integer> {
    // 활성화된 배너를 순서대로 가져오기 (사용자 화면용)
    List<Banner> findByActiveOrderByPriorityAsc(boolean active);
}