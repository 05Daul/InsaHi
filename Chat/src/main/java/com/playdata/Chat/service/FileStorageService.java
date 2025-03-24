package com.playdata.Chat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    public String saveFile(MultipartFile file) {
        File uploadPath = new File(UPLOAD_DIR);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();  // ✅ 폴더가 없으면 생성
        }

        try {
            // 1) UUID + 원본 파일명을 합쳐 "실제 저장 파일명" 만들기
            String uuid = UUID.randomUUID().toString();

            // 2) 파일명 안전 치환(공백, 특수문자 -> '_')
            String originalFileName = file.getOriginalFilename();
//            String safeName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");

            // 3) 최종 저장될 파일명 "UUID_치환된이름"
            String storedFileName = uuid + "_" + originalFileName;

            // 4) 디스크에 저장
            File targetFile = new File(uploadPath, storedFileName);
            file.transferTo(targetFile);
            String encodedFileName = URLEncoder.encode(storedFileName, StandardCharsets.UTF_8)
                    .replace("+", "%20");
            // 5) 브라우저에서 접근할 URL. (예: /files/ + 실제저장파일명)
            String fileUrl = "/files/" + encodedFileName;

            System.out.println("UPLOAD_DIR => " + UPLOAD_DIR);
            System.out.println("SAVED FILE => " + storedFileName);
            System.out.println("FILE PATH => " + new File(UPLOAD_DIR, storedFileName).getAbsolutePath());
            System.out.println("EXISTS? => " + new File(UPLOAD_DIR, storedFileName).exists());

            // 6) 파이프로 구분해서 반환: "접근URL|원본파일명"
            //    => 프론트에서 split("|")로 나눠서 사용
            return fileUrl + "|" + originalFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public File getFile(String storedFileName) {
        return new File(UPLOAD_DIR, storedFileName);

    }
}
