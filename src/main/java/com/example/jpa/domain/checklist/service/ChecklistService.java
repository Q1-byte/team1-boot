package com.example.jpa.domain.checklist.service;

import com.example.jpa.domain.checklist.dto.ChecklistDto;
import com.example.jpa.domain.checklist.entity.Checklist;
import com.example.jpa.domain.checklist.repository.ChecklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChecklistService {

    private final ChecklistRepository checklistRepository;

    // 등록
    @Transactional
    public void insert(ChecklistDto dto) {
        Checklist checklist = Checklist.builder()
                .task(dto.getTask())
                .mandatory(dto.isMandatory())
                .priority(dto.getPriority())
                .build();
        checklistRepository.save(checklist);
    }

    // 수정
    @Transactional
    public void update(int id, ChecklistDto dto) {
        Checklist checklist = checklistRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("항목을 찾을 수 없습니다."));
        checklist.update(dto.getTask(), dto.isMandatory(), dto.getPriority());
    }

    // 목록 조회 (DTO 변환 포함)
    public List<ChecklistDto> findAll() {
        return checklistRepository.findAllByOrderByPriorityAsc().stream()
                .map(c -> ChecklistDto.builder()
                        .id(c.getId())
                        .task(c.getTask())
                        .mandatory(c.isMandatory())
                        .priority(c.getPriority())
                        .build())
                .collect(Collectors.toList());
    }

    // 삭제
    @Transactional
    public void delete(int id) {
        checklistRepository.deleteById(id);
    }
}