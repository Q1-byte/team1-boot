package com.example.jpa.domain.banner.controller;

import com.example.jpa.domain.banner.dto.BannerDto;
import com.example.jpa.domain.banner.entity.Banner;
import com.example.jpa.domain.banner.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/banners")
public class AdminBannerController {

    private final BannerService bannerService;

    @PostMapping // 배너 등록
    public ResponseEntity<Void> addBanner(@RequestBody BannerDto.Request request) {
        Banner banner = new Banner();

        banner.setTitle(request.getTitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLinkUrl(request.getLinkUrl());

        bannerService.insert(banner);

        return ResponseEntity.ok().build();
    }

    @GetMapping // 현재 게시 중인 배너 목록
    public List<Banner> getBanners() {
        return bannerService.findByAll();
    }
}