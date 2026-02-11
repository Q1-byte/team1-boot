package com.example.jpa.domain.event.controller;

import com.example.jpa.domain.event.dto.EventDto;
import com.example.jpa.domain.event.service.EventService;
import com.example.jpa.domain.event.type.EventCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * 1. 목록/검색/필터링 조회 (페이징 포함)
     * 프론트엔드 리스트 화면에서 사용합니다.
     */
    @GetMapping
    public ResponseEntity<Page<EventDto>> getList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) EventCategory type,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("Log4j2 - [목록 조회 요청] 검색어: {}, 카테고리: {}", name, type);
        return ResponseEntity.ok(eventService.getEvents(name, type, pageable));
    }

    /**
     * 2. 상세 조회
     * 카드 클릭 시 상세 페이지로 이동할 때 사용합니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getDetail(@PathVariable Long id) {
        log.info("Log4j2 - [상세 조회 요청] ID: {}", id);
        return ResponseEntity.ok(eventService.getEventDetail(id));
    }

    /**
     * 3. 등록 (CRUD - Create)
     * 관리자 페이지 등에서 새로운 행사를 수동 등록할 때 사용합니다.
     */
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody EventDto dto) {
        log.info("Log4j2 - [등록 요청] 행사명: {}", dto.getName());
        return ResponseEntity.ok(eventService.createEvent(dto));
    }

    /**
     * 4. 수정 (CRUD - Update)
     * 기존 행사 정보나 목업 데이터를 수정할 때 사용합니다.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody EventDto dto) {
        log.info("Log4j2 - [수정 요청] ID: {}, 수정 내용: {}", id, dto.getName());
        eventService.updateEvent(id, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 5. 삭제 (CRUD - Delete)
     * 불필요한 테스트 데이터나 종료된 행사를 삭제할 때 사용합니다.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Log4j2 - [삭제 요청] ID: {}", id);
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}