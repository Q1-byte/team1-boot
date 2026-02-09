package com.example.jpa.global.file.controller;

import com.example.jpa.global.file.dto.FileResponseDto;
import com.example.jpa.global.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<List<FileResponseDto>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        List<FileResponseDto> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            responses.add(fileService.uploadFile(file));
        }
        return ResponseEntity.ok(responses);
    }
}
