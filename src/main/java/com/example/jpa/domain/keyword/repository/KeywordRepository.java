package com.example.jpa.domain.keyword.repository;

import com.example.jpa.domain.keyword.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Integer> {


    Optional<Keyword> findByName(String name);

}