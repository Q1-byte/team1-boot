package com.example.jpa.domain.mypage.dto;

import com.example.jpa.domain.history.dto.ViewHistoryResponseDto;
import com.example.jpa.domain.user.dto.UserResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponseDto {

    private UserResponseDto user;
    private List<ViewHistoryResponseDto> recentViewedPlans;
}
