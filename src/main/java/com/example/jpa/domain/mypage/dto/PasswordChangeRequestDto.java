package com.example.jpa.domain.mypage.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordChangeRequestDto {

    private String currentPassword;
    private String newPassword;
}
