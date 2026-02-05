package com.example.jpa.domain.history.service;

import com.example.jpa.domain.history.entity.History;
import com.example.jpa.domain.history.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HistoryService {

    private final HistoryRepository historyRepository;

    /**
     * 히스토리 생성 (다른 서비스에서 호출해서 사용)
     */
    @Transactional
    public void log(String type, String content, Integer targetId) {
        History history = History.builder()
                .type(type)
                .content(content)
                .targetId(targetId)
                .build();

        historyRepository.save(history);
    }

    /**
     * 전체 히스토리 조회 (관리자 페이지용)
     */
    public List<History> findAll() {
        return historyRepository.findAll();
    }
    /**
     * 특정 타입별 히스토리 조회 (필터링)
     */
    public List<History> getHistoriesByType(String type) {
        return historyRepository.findByType(type);
    }
    @Transactional
    public Integer saveHistory(History history) {
        History saved = historyRepository.save(history);
        return saved.getId();
    }
}