package com.example.jpa.domain.banner.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {
    private Integer id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private int priority;
    private boolean active;

    @Getter @Setter // <--- 여기에 @Getter가 있어야 컨트롤러에서 꺼내 쓸 수 있어요!
    public static class Request {
        private String title;
        private String imageUrl;
        private String linkUrl;
    }
}