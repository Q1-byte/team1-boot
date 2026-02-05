package com.example.jpa.domain.event.service;

import com.example.jpa.domain.event.entity.Event;
import com.example.jpa.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public Long saveEvent(Event event) {
        return eventRepository.save(event).getId();
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }
    // 수정, 상세조회, 삭제 로직 추가...
}