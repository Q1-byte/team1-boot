package com.example.jpa.domain.content.service;

import com.example.jpa.domain.content.dto.ContentDto;
import com.example.jpa.domain.content.entity.Content;
import com.example.jpa.domain.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContentService {

    private final ContentRepository contentRepository;

    // 등록
    @Transactional
    public void insert(ContentDto dto) {
        Content content = Content.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .category(dto.getCategory())
                .build();
        contentRepository.save(content);
    }

    // 수정
    @Transactional
    public void update(int id, ContentDto dto) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("컨텐츠가 없습니다."));
        content.update(dto.getTitle(), dto.getBody(), dto.getCategory());
    }

    // 상세 조회 (조회수 증가 포함)
    @Transactional
    public ContentDto findById(int id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("컨텐츠가 없습니다."));
        content.incrementViewCount(); // 조회수 1 증가

        return ContentDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .body(content.getBody())
                .category(content.getCategory())
                .viewCount(content.getViewCount())
                .regDate(content.getRegDate())
                .build();
    }

    // 전체 목록 조회 (페이징)
    public Page<Content> findAll(Pageable pageable) {
        return contentRepository.findAll(pageable);
    }

    // 삭제
    @Transactional
    public void delete(int id) {
        contentRepository.deleteById(id);
    }
}