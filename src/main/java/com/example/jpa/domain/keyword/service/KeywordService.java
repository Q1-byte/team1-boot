package com.example.jpa.domain.keyword.service;

import com.example.jpa.domain.history.service.HistoryService;
import com.example.jpa.domain.keyword.entity.Keyword;
import com.example.jpa.domain.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final HistoryService historyService;

    // 1. findAll 에러 해결
    public List<Keyword> findAll() {
        return keywordRepository.findAll();
    }

    // 2. saveKeyword 에러 해결 (반환 타입을 Long으로 맞춤)
    @Transactional
    public Long saveKeyword(Keyword keyword) {
        Keyword saved = keywordRepository.save(keyword);

        // 히스토리 기록 (Long 타입을 Integer로 변환하여 기록)
        historyService.log("KEYWORD_REG", "키워드 등록: " + saved.getName(), saved.getId().intValue());

        return saved.getId().longValue();
    }

    // 3. deleteKeyword 에러 해결
    @Transactional
    public void deleteKeyword(Long id) {
        Keyword keyword = keywordRepository.findById(id.intValue()) // Integer ID인 경우 변환 필요
                .orElseThrow(() -> new IllegalArgumentException("해당 키워드가 없습니다."));

        keywordRepository.delete(keyword);

        // 히스토리 기록
        historyService.log("KEYWORD_DEL", "키워드 삭제: " + keyword.getName(), id.intValue());
        historyService.log("KEYWORD_CREATE", "키워드 신규 등록: " + keyword.getName(), keyword.getId());
    }
    /**
     * 모든 키워드 조회
     */
    public List<Keyword> getAllKeywords() {
        return keywordRepository.findAll();
    }
}