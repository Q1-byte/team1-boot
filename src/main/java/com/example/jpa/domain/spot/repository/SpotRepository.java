package com.example.jpa.domain.spot.repository;

import com.example.jpa.domain.spot.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
    boolean existsByName(String name);


}
