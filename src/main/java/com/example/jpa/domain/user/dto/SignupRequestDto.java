package com.example.jpa.domain.user.dto;

import com.example.jpa.domain.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {
    
    private String username;
    private String nickname;
    private String password;
    private String passwordConfirm;
    private String email;
    private String phone;
    private String keywordPref;
    
    // DTO -> Entity 변환
    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(this.username)
                .nickname(this.nickname)
                .password(encodedPassword)
                .email(this.email)
                .phone(this.phone)
                .keywordPref(this.keywordPref)
                .role("USER")
                .point(0)
                .build();
    }
}
