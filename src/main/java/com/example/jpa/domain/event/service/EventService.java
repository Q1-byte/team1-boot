package com.example.jpa.domain.event.service;

import com.example.jpa.domain.event.dto.EventDto;
import com.example.jpa.domain.event.entity.Event;
import com.example.jpa.domain.event.repository.EventRepository;
import com.example.jpa.domain.event.type.EventCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    /**
     * 목록 조회: 이름 검색이나 카테고리 필터링이 가능하도록 유지합니다.
     */
    @Transactional(readOnly = true)
    public Page<EventDto> getEvents(String name, EventCategory type, Pageable pageable) {
        Page<Event> page;
        boolean hasName = name != null && !name.isEmpty();
        boolean hasType = type != null;

        if (hasName && hasType) {
            page = eventRepository.findByNameContainingAndCat2(name, type.getDescription(), pageable);
        } else if (hasName) {
            page = eventRepository.findByNameContaining(name, pageable);
        } else if (hasType) {
            page = eventRepository.findByCat2(type.getDescription(), pageable);
        } else {
            page = eventRepository.findAll(pageable);
        }
        return page.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public EventDto getEventDetail(Long id) {
        return eventRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new IllegalArgumentException("행사를 찾을 수 없습니다. ID: " + id));
    }

    /**
     * 신규 등록: 사용자님이 직접 INSERT 하신 것처럼 고퀄리티 데이터를 추가합니다.
     */
    @Transactional
    public Long createEvent(EventDto dto) {
        Event event = new Event();
        mapDtoToEntity(dto, event);
        Event saved = eventRepository.save(event);
        log.info("Log4j2 - [등록성공] 새로운 축제 데이터가 생성되었습니다. ID: {}", saved.getId());
        return saved.getId();
    }

    /**
     * 수정: "테스트 행사" 등을 실제 축제 구도에 맞게 필드 하나하나 정교하게 수정합니다.
     */
    @Transactional
    public void updateEvent(Long id, EventDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 데이터가 없습니다. ID: " + id));

        // 프론트엔드 구도에 필요한 모든 필드를 업데이트합니다.
        mapDtoToEntity(dto, event);
        log.info("Log4j2 - [수정성공] ID: {} 의 정보가 업데이트되었습니다.", id);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제할 데이터가 이미 없습니다.");
        }
        eventRepository.deleteById(id);
        log.info("Log4j2 - [삭제성공] ID: {} 데이터가 제거되었습니다.", id);
    }

    private EventDto convertToDto(Event e) {
        return EventDto.builder()
                .id(e.getId())
                .contentId(e.getContentId())
                .name(e.getName())
                .address(e.getAddress())
                .addr2(e.getAddr2())
                .zipCode(e.getZipCode())
                .tel(e.getTel())
                .url(e.getImageUrl()) // [변경] imageUrl -> url
                .cat1(e.getCat1())
                .category(e.getCat2()) // [변경] cat2 -> category
                .description(e.getDescription()) // [추가]
                .cat3(e.getCat3())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .mapX(e.getMapX())
                .mapY(e.getMapY())
                .build();
    }

    /**
     * 핵심: DTO 데이터를 Entity로 옮길 때 '이미지', '좌표', '전화번호' 등을 누락 없이 매핑합니다.
     */
    private void mapDtoToEntity(EventDto d, Event e) {
        e.setName(d.getName());
        e.setAddress(d.getAddress());
        e.setAddr2(d.getAddr2());
        e.setZipCode(d.getZipCode());
        e.setTel(d.getTel());
        e.setCat1(d.getCat1());
        e.setCat3(d.getCat3());
        e.setStartDate(d.getStartDate());
        e.setEndDate(d.getEndDate());
        e.setImageUrl(d.getUrl()); // [변경] url -> imageUrl
        e.setDescription(d.getDescription()); // [추가]
        e.setCat2(d.getCategory()); // [변경] category -> cat2
        e.setMapX(d.getMapX());
        e.setMapY(d.getMapY());
        // contentId는 수동 입력 데이터에서도 고유값으로 쓰일 수 있으므로 null 체크 후 매핑
        if (d.getContentId() != null)
            e.setContentId(d.getContentId());
    }
}