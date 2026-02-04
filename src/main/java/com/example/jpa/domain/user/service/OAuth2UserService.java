package com.example.jpa.domain.user.service;

import com.example.jpa.domain.user.entity.User;
import com.example.jpa.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Provider별 사용자 정보 추출
        final String email;
        final String name;

        if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount == null) {
                throw new OAuth2AuthenticationException("카카오 계정 정보가 없습니다.");
            }
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            // 이메일 권한이 없거나 비동의 시 ID로 임시 이메일 생성
            String tempEmail = (String) kakaoAccount.get("email");
            if (tempEmail == null || tempEmail.isEmpty()) {
                String id = String.valueOf(attributes.get("id"));
                email = id + "@kakao.com";
            } else {
                email = tempEmail;
            }

            name = profile != null ? (String) profile.get("nickname") : null;
            if (name == null) {
                throw new OAuth2AuthenticationException("카카오 사용자 정보가 부족합니다.");
            }
        } else {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            if (name == null) {
                throw new OAuth2AuthenticationException("사용자 정보가 부족합니다.");
            }
        }

        log.info("OAuth2 로그인: provider={}, email={}, name={}", provider, email, name);

        // 기존 사용자 조회 또는 새로 생성
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createOAuth2User(email, name, provider));

        log.info("OAuth2 사용자: id={}, username={}", user.getId(), user.getUsername());

        // 커스텀 attributes 생성 (email을 키로 사용하기 위해)
        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("email", email);
        customAttributes.put("name", name);

        String userNameAttribute = "kakao".equals(provider) ? "id" : "email";

        return new DefaultOAuth2User(
                Collections.singleton(() -> "ROLE_" + user.getRole()),
                customAttributes,
                userNameAttribute);
    }

    private User createOAuth2User(String email, String name, String provider) {
        // 유니크한 username 생성
        String username = provider + "_" + email.split("@")[0];

        User newUser = User.builder()
                .username(username)
                .password("") // OAuth 사용자는 비밀번호 없음
                .email(email)
                .role("USER")
                .point(0)
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("새 OAuth2 사용자 생성: {}", savedUser.getUsername());

        return savedUser;
    }
}
