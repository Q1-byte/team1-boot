package com.example.jpa.domain.region.service;

import com.example.jpa.domain.region.entity.Region;
import com.example.jpa.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {
    private final RegionRepository regionRepository;

    @Transactional
    public Long saveRegion(Region region) {
        return regionRepository.save(region).getId();
    }

    public List<Region> findAll() {
        return regionRepository.findAll();
    }
}