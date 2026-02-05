package com.example.jpa.domain.mypage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequestDto {

    private String phone;
    private String keywordPref;
}
