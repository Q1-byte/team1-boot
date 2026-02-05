package com.example.jpa.domain.checklist.repository;

import com.example.jpa.domain.checklist.entity.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer> {
    // 우선순위 순으로 정렬해서 가져오기
    List<Checklist> findAllByOrderByPriorityAsc();
}