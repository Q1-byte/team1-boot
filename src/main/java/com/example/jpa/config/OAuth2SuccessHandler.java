package com.example.jpa.config;

import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Log4j2
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private static final String FRONTEND_URL = "http://localhost:5173";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // email 가져오기 (구글, 카카오 모두 customAttributes에 저장됨)
        String email = oAuth2User.getAttribute("email");

        log.info("OAuth2 로그인 성공: {}", email);

        // DB에서 사용자 정보 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 임시 토큰 생성 (프론트엔드와 동일한 방식)
        String tokenData = String.format(
                "{\"userId\":%d,\"username\":\"%s\",\"role\":\"%s\",\"exp\":%d}",
                user.getId(),
                user.getUsername(),
                user.getRole(),
                System.currentTimeMillis() + 24 * 60 * 60 * 1000
        );
        String token = Base64.getEncoder().encodeToString(tokenData.getBytes(StandardCharsets.UTF_8));

        // 사용자 정보를 JSON으로 인코딩
        String userData = String.format(
                "{\"id\":%d,\"username\":\"%s\",\"email\":\"%s\",\"role\":\"%s\",\"point\":%d}",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getPoint() != null ? user.getPoint() : 0
        );
        String encodedUser = URLEncoder.encode(userData, StandardCharsets.UTF_8);

        // 프론트엔드로 리다이렉트 (토큰과 사용자 정보 전달)
        String redirectUrl = FRONTEND_URL + "/oauth/callback?token=" + token + "&user=" + encodedUser;

        log.info("리다이렉트: {}", FRONTEND_URL + "/oauth/callback");

        response.sendRedirect(redirectUrl);
    }
}
