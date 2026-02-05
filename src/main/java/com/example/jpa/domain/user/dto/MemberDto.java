package com.example.jpa.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private Integer id;
    private String username;
    private String email;
    private String role; // ADMIN, USER 구분
}
