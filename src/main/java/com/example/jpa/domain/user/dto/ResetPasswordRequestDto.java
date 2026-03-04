package com.example.jpa.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequestDto {
    private String username;
    private String newPassword;
}
