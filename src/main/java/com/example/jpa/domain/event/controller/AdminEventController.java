package com.example.jpa.domain.event.controller;

import com.example.jpa.domain.event.dto.EventDto;
import com.example.jpa.domain.event.entity.Event;
import com.example.jpa.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> list() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody EventDto.Request request) {
        Event event = new Event();

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());

        return ResponseEntity.ok(eventService.saveEvent(event));
    }
}