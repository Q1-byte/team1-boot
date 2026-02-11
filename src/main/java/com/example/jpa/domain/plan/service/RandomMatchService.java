package com.example.jpa.domain.plan.service;

import com.example.jpa.domain.accommodation.dto.AccommodationDto;
import com.example.jpa.domain.accommodation.entity.Accommodation;
import com.example.jpa.domain.accommodation.service.AccommodationService;
import com.example.jpa.domain.activity.dto.ActivityDto;
import com.example.jpa.domain.activity.entity.Activity;
import com.example.jpa.domain.activity.service.ActivityService;
import com.example.jpa.domain.plan.dto.RandomMatchRequestDto;
import com.example.jpa.domain.plan.dto.RandomMatchResponseDto;
import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import com.example.jpa.domain.plan.entity.TravelPlan;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import com.example.jpa.domain.ticket.dto.TicketDto;
import com.example.jpa.domain.ticket.entity.Ticket;
import com.example.jpa.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomMatchService {

    private final AccommodationService accommodationService;
    private final ActivityService activityService;
    private final TicketService ticketService;
    private final TravelPlanRepository travelPlanRepository;

    /**
     * 랜덤 여행 계획 생성 (최소 1박2일)
     *
     * 예산 배분 전략:
     *   숙소 = 전체 예산의 40% (1박 기준으로 환산)
     *   액티비티 = 전체 예산의 35%
     *   티켓 = 전체 예산의 25%
     */
    @Transactional
    public RandomMatchResponseDto generateRandomPlan(Long userId, RandomMatchRequestDto request) {

        Long regionId = request.getRegionId();
        Integer budgetMax = request.getBudgetMax() != null ? request.getBudgetMax() : Integer.MAX_VALUE;
        Integer budgetMin = request.getBudgetMin() != null ? request.getBudgetMin() : 0;
        String keyword = request.getKeyword();
        LocalDate travelDate = request.getTravelDate() != null ? request.getTravelDate() : LocalDate.now();
        int durationDays = request.getDurationDays() != null ? Math.max(request.getDurationDays(), 2) : 2;
        int nights = durationDays - 1; // 1박2일 → 1박, 2박3일 → 2박

        // ── 1) 예산 배분 ──
        int accoBudgetPerNight = (int) (budgetMax * 0.4) / nights;
        int activityBudgetTotal = (int) (budgetMax * 0.35);
        int ticketBudgetTotal = (int) (budgetMax * 0.25);

        // ── 2) 숙소 매칭 (항상 1건) ──
        Accommodation acco;
        if (keyword != null && !keyword.isBlank()) {
            acco = accommodationService.findRandomMatchWithKeyword(
                    regionId, budgetMin > 0 ? (int)(budgetMin * 0.2) : 0, accoBudgetPerNight, keyword);
        } else {
            acco = accommodationService.findRandomMatch(regionId, 0, accoBudgetPerNight);
        }
        AccommodationDto accoDto = acco != null ? AccommodationDto.fromEntity(acco) : null;
        int accoTotalPrice = acco != null ? acco.getPricePerNight() * nights : 0;

        // ── 3) 액티비티 매칭 (일수만큼) ──
        int activityCount = durationDays;
        List<Activity> activities;
        if (keyword != null && !keyword.isBlank()) {
            activities = activityService.findRandomMatchesWithKeyword(
                    regionId, activityBudgetTotal / activityCount, keyword, activityCount);
        } else {
            activities = activityService.findRandomMatches(
                    regionId, activityBudgetTotal / activityCount, activityCount);
        }
        List<ActivityDto> activityDtos = activities.stream()
                .map(ActivityDto::fromEntity)
                .toList();
        int activityTotalPrice = activities.stream()
                .mapToInt(Activity::getPrice)
                .sum();

        // ── 4) 티켓 매칭 (일수 기준 절반, 최소 1건) ──
        int ticketCount = Math.max(durationDays / 2, 1);
        List<Ticket> tickets;
        if (keyword != null && !keyword.isBlank()) {
            tickets = ticketService.findRandomMatchesWithKeyword(
                    regionId, ticketBudgetTotal / ticketCount, keyword, ticketCount);
        } else {
            tickets = ticketService.findRandomMatches(
                    regionId, ticketBudgetTotal, travelDate, ticketCount);
        }
        List<TicketDto> ticketDtos = tickets.stream()
                .map(TicketDto::fromEntity)
                .toList();
        int ticketTotalPrice = tickets.stream()
                .mapToInt(Ticket::getPrice)
                .sum();

        // ── 5) 예상 총 비용 계산 ──
        int estimatedTotal = accoTotalPrice + activityTotalPrice + ticketTotalPrice;

        // ── 6) TravelPlan 저장 ──
        TravelPlan travelPlan = TravelPlan.builder()
                .userId(userId)
                .type("RANDOM")
                .title(nights + "박" + durationDays + "일 랜덤 여행")
                .regionId(regionId)
                .keyword(keyword)
                .budgetMin(budgetMin)
                .budgetMax(budgetMax)
                .travelDate(travelDate)
                .durationDays(durationDays)
                .peopleCount(request.getPeopleCount() != null ? request.getPeopleCount() : 1)
                .totalPrice(estimatedTotal)
                .status("READY")
                .build();
        travelPlanRepository.save(travelPlan);

        // ── 7) 응답 조립 ──
        return RandomMatchResponseDto.builder()
                .plan(TravelPlanResponseDto.fromEntity(travelPlan))
                .accommodation(accoDto)
                .activities(activityDtos)
                .tickets(ticketDtos)
                .estimatedTotalPrice(estimatedTotal)
                .build();
    }

    /**
     * 기존 TravelPlan 기반으로 다시 랜덤 매칭 (re-random)
     */
    @Transactional
    public RandomMatchResponseDto reRandom(Long travelPlanId) {
        TravelPlan existing = travelPlanRepository.findById(travelPlanId)
                .orElseThrow(() -> new IllegalArgumentException("여행 계획을 찾을 수 없습니다: " + travelPlanId));

        existing.setReRandomCount(existing.getReRandomCount() + 1);

        Long regionId = existing.getRegionId();
        Integer budgetMax = existing.getBudgetMax() != null ? existing.getBudgetMax() : Integer.MAX_VALUE;
        String keyword = existing.getKeyword();
        LocalDate travelDate = existing.getTravelDate() != null ? existing.getTravelDate() : LocalDate.now();
        int durationDays = existing.getDurationDays() != null ? Math.max(existing.getDurationDays(), 2) : 2;
        int nights = durationDays - 1;

        int accoBudgetPerNight = (int) (budgetMax * 0.4) / nights;
        int activityBudgetTotal = (int) (budgetMax * 0.35);
        int ticketBudgetTotal = (int) (budgetMax * 0.25);

        // 숙소
        Accommodation acco = (keyword != null && !keyword.isBlank())
                ? accommodationService.findRandomMatchWithKeyword(regionId, 0, accoBudgetPerNight, keyword)
                : accommodationService.findRandomMatch(regionId, 0, accoBudgetPerNight);
        AccommodationDto accoDto = acco != null ? AccommodationDto.fromEntity(acco) : null;
        int accoTotalPrice = acco != null ? acco.getPricePerNight() * nights : 0;

        // 액티비티
        int activityCount = durationDays;
        List<Activity> activities = (keyword != null && !keyword.isBlank())
                ? activityService.findRandomMatchesWithKeyword(regionId, activityBudgetTotal / activityCount, keyword, activityCount)
                : activityService.findRandomMatches(regionId, activityBudgetTotal / activityCount, activityCount);

        // 티켓
        int ticketCount = Math.max(durationDays / 2, 1);
        List<Ticket> tickets = (keyword != null && !keyword.isBlank())
                ? ticketService.findRandomMatchesWithKeyword(regionId, ticketBudgetTotal / ticketCount, keyword, ticketCount)
                : ticketService.findRandomMatches(regionId, ticketBudgetTotal, travelDate, ticketCount);

        int estimatedTotal = accoTotalPrice
                + activities.stream().mapToInt(Activity::getPrice).sum()
                + tickets.stream().mapToInt(Ticket::getPrice).sum();

        existing.setTotalPrice(estimatedTotal);
        travelPlanRepository.save(existing);

        return RandomMatchResponseDto.builder()
                .plan(TravelPlanResponseDto.fromEntity(existing))
                .accommodation(accoDto)
                .activities(activities.stream().map(ActivityDto::fromEntity).toList())
                .tickets(tickets.stream().map(TicketDto::fromEntity).toList())
                .estimatedTotalPrice(estimatedTotal)
                .build();
    }
}
