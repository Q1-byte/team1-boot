package com.example.jpa.domain.user.dto;

import com.example.jpa.domain.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String role;
    private String keywordPref;
    private Integer point;
    private LocalDateTime createdAt;
    
    // Entity -> DTO 변환
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .keywordPref(user.getKeywordPref())
                .point(user.getPoint())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
