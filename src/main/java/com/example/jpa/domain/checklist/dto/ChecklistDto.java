package com.example.jpa.domain.checklist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChecklistDto {
    private Integer id;
    private String task;
    private boolean mandatory;
    private int priority;
}