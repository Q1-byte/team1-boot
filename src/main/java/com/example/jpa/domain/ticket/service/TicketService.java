package com.example.jpa.domain.ticket.service;

import com.example.jpa.domain.ticket.entity.Ticket;
import com.example.jpa.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;

    public List<Ticket> findByRegion(Long regionId) {
        return ticketRepository.findByRegionIdAndIsActiveTrue(regionId);
    }

    public List<Ticket> findByRegionAndMaxPrice(Long regionId, Integer maxPrice) {
        return ticketRepository.findByRegionIdAndPriceLessThanEqualAndIsActiveTrue(regionId, maxPrice);
    }

    public List<Ticket> findByRegionAndCategory(Long regionId, String category) {
        return ticketRepository.findByRegionIdAndCategoryAndIsActiveTrue(regionId, category);
    }

    public List<Ticket> findAvailableByRegionAndDate(Long regionId, LocalDate date) {
        return ticketRepository.findAvailableByRegionAndDate(regionId, date);
    }

    /**
     * 랜덤 매칭: 지역 + 예산 + 여행날짜 기준 이용 가능한 티켓 중 랜덤 N건
     */
    public List<Ticket> findRandomMatches(Long regionId, Integer maxPrice, LocalDate travelDate, int count) {
        // 먼저 날짜 + 지역으로 이용 가능한 티켓 조회
        List<Ticket> candidates = ticketRepository.findAvailableByRegionAndDate(regionId, travelDate);
        // 예산 필터
        if (maxPrice != null) {
            candidates = candidates.stream()
                    .filter(t -> t.getPrice() <= maxPrice)
                    .toList();
        }
        if (candidates.isEmpty()) {
            candidates = ticketRepository.findByRegionIdAndIsActiveTrue(regionId);
        }
        if (candidates.isEmpty()) return List.of();
        List<Ticket> shuffled = new ArrayList<>(candidates);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    /**
     * 랜덤 매칭: 지역 + 예산 + 키워드 조건으로 랜덤 N건
     */
    public List<Ticket> findRandomMatchesWithKeyword(Long regionId, Integer maxPrice, String keyword, int count) {
        List<Ticket> candidates = ticketRepository
                .findByRegionAndPriceAndKeyword(regionId, maxPrice, keyword);
        if (candidates.isEmpty()) {
            candidates = ticketRepository.findByRegionIdAndPriceLessThanEqualAndIsActiveTrue(regionId, maxPrice);
        }
        if (candidates.isEmpty()) {
            candidates = ticketRepository.findByRegionIdAndIsActiveTrue(regionId);
        }
        if (candidates.isEmpty()) return List.of();
        List<Ticket> shuffled = new ArrayList<>(candidates);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

    public Ticket findOne(Long id) {
        return ticketRepository.findById(id).orElseThrow();
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }
}
