package com.example.jpa.domain.mypage.service;

import com.example.jpa.domain.history.dto.ViewHistoryResponseDto;
import com.example.jpa.domain.history.entity.ViewHistory;
import com.example.jpa.domain.history.repository.ViewHistoryRepository;
import com.example.jpa.domain.mypage.dto.MyPageResponseDto;
import com.example.jpa.domain.mypage.dto.PasswordChangeRequestDto;
import com.example.jpa.domain.mypage.dto.ProfileUpdateRequestDto;
import com.example.jpa.domain.plan.dto.TravelPlanResponseDto;
import com.example.jpa.domain.plan.entity.TravelPlan;
import com.example.jpa.domain.plan.repository.TravelPlanRepository;
import com.example.jpa.domain.point.dto.PointResponseDto;
import com.example.jpa.domain.point.repository.PointRepository;
import com.example.jpa.domain.user.dto.UserResponseDto;
import com.example.jpa.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final UserService userService;
    private final TravelPlanRepository travelPlanRepository;
    private final PointRepository pointRepository;
    private final ViewHistoryRepository viewHistoryRepository;

    /**
     * 마이페이지 메인 조회 (회원정보 + 최근 본 계획 3개)
     */
    public MyPageResponseDto getMyPageMain(Long userId) {
        UserResponseDto user = userService.findById(userId);
        List<ViewHistoryResponseDto> recentViewedPlans = getRecentViewedPlans(userId);

        return MyPageResponseDto.builder()
                .user(user)
                .recentViewedPlans(recentViewedPlans)
                .build();
    }

    /**
     * 최근 본 계획 3개 조회
     */
    private List<ViewHistoryResponseDto> getRecentViewedPlans(Long userId) {
        List<ViewHistory> histories = viewHistoryRepository.findTop3ByUserIdOrderByViewedAtDesc(userId);
        List<ViewHistoryResponseDto> result = new ArrayList<>();

        for (ViewHistory history : histories) {
            TravelPlan plan = travelPlanRepository.findById(history.getPlanId()).orElse(null);
            if (plan != null) {
                TravelPlanResponseDto planDto = TravelPlanResponseDto.fromEntity(plan);
                result.add(ViewHistoryResponseDto.fromEntityWithPlan(history, planDto));
            } else {
                result.add(ViewHistoryResponseDto.fromEntity(history));
            }
        }

        return result;
    }

    /**
     * 내 여행 계획 목록 조회
     */
    public List<TravelPlanResponseDto> getMyPlans(Long userId) {
        return travelPlanRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(TravelPlanResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 내 포인트 내역 조회
     */
    public List<PointResponseDto> getMyPoints(Long userId) {
        return pointRepository.findByUserIdOrderByCreatedAtDesc(userId.intValue())
                .stream()
                .map(PointResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 프로필 수정 (연락처, 여행성향)
     */
    @Transactional
    public UserResponseDto updateProfile(Long userId, ProfileUpdateRequestDto requestDto) {
        return userService.updateUser(userId, requestDto.getPhone(), requestDto.getKeywordPref());
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long userId, PasswordChangeRequestDto requestDto) {
        userService.changePassword(userId, requestDto.getCurrentPassword(), requestDto.getNewPassword());
    }
}
