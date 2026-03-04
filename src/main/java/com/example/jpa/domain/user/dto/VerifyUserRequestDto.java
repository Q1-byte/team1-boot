package com.example.jpa.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyUserRequestDto {
    private String username;
    private String email;
}
