package com.example.jpa.domain.keyword.entity;

import com.example.jpa.domain.plan.entity.TravelSpot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "spot_keyword")
public class SpotKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    @JsonIgnore // 🚀 순환 참조 방지를 위해 추가!
    private TravelSpot spot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Keyword keyword;
}