package com.playdata.Common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUpload {

    private final String uploadPath = "C:\\InsaHi\\insafront\\src\\img";

    // 파일 저장
    public String saveFile(MultipartFile multipartFile, String category) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일 업로드를 해주세요.");
        }

        // 카테고리별 폴더 경로 설정
        String categoryPath = uploadPath + File.separator + category;
        java.io.File categoryDir = new java.io.File(categoryPath);
        if (!categoryDir.exists()) {
            categoryDir.mkdirs();
        }

        String originalFileName = multipartFile.getOriginalFilename(); // 원본 파일명
        String storeFilename = createStoreFilename(originalFileName); // UUID로 저장할 파일명 생성

        java.io.File targetFile = new java.io.File(categoryPath + File.separator + storeFilename);
        multipartFile.transferTo(targetFile);

        // 저장된 파일 경로를 반환 (category/파일명 형태로)
        return category + "/" + storeFilename;
    }

    // 파일 삭제
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("삭제할 파일이 없습니다.");
        }

        java.io.File file = new java.io.File(uploadPath + File.separator + filePath);
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                throw new RuntimeException("모종의 이유로 파일 삭제에 실패했습니다.");
            }
        }
    }

    // 파일 업데이트
    public String updateFile(MultipartFile newFile, String existingFilePath, String category) throws IOException {
        if (existingFilePath != null && !existingFilePath.isEmpty()) {
            deleteFile(existingFilePath);
        }

        return saveFile(newFile, category);
    }

    // 파일명 생성 (UUID 기반)
    private String createStoreFilename(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 파일 불러오기 (경로로 파일 읽기)
    public byte[] loadFile(String filePath) throws IOException {
        Path path = Paths.get(uploadPath + File.separator + filePath);
        if (Files.exists(path)) {
            return Files.readAllBytes(path);
        } else {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }
    }
}
