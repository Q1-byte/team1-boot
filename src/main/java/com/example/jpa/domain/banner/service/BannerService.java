package com.example.jpa.domain.banner.service;

import com.example.jpa.domain.banner.entity.Banner;
import com.example.jpa.domain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;

    // 등록
    @Transactional
    public void insert(Banner banner) {
        bannerRepository.save(banner);
    }

    // 수정
    @Transactional
    public void update(int id, Banner bannerDto) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 배너가 없습니다."));

        banner.changeInfo(
                bannerDto.getTitle(),
                bannerDto.getImageUrl(),
                bannerDto.getLinkUrl(),
                bannerDto.getPriority(),
                bannerDto.isActive()
        );
    }

    // 삭제
    @Transactional
    public void delete(int id) {
        bannerRepository.deleteById(id);
    }

    // 전체 조회 (관리자용)
    public List<Banner> findByAll() {
        return bannerRepository.findAll();
    }

    // 활성화된 배너만 조회 (리액트 메인 화면용)
    public List<Banner> findActiveBanners() {
        return bannerRepository.findByActiveOrderByPriorityAsc(true);
    }
}