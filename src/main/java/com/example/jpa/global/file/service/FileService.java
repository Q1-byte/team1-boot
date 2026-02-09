package com.example.jpa.global.file.service;

import com.example.jpa.global.file.dto.FileResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    // 로컬 저장 경로 (C:/team1/uploads 처럼 실제 존재하는 경로 권장)
    private final String uploadPath = "C:/tmp/project_uploads/";

    public FileResponseDto uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }

        String originName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = "";

        if (originName != null && originName.contains(".")) {
            extension = originName.substring(originName.lastIndexOf("."));
        }

        String storedName = uuid + extension;
        File dest = new File(uploadPath + storedName);

        // 폴더가 없으면 생성
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
        }

        // 프론트에서 접근 가능한 URL 반환 (WebConfig에서 /api/files/** 로 매핑 예정)
        String storedUrl = "http://localhost:8080/api/files/" + storedName;

        return FileResponseDto.builder()
                .originName(originName)
                .storedUrl(storedUrl)
                .build();
    }
}
