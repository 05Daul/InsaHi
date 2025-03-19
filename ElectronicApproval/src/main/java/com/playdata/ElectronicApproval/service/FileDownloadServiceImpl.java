package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.common.publicEntity.FileEntity;
import com.playdata.ElectronicApproval.dao.FileDAO;
import com.playdata.ElectronicApproval.dto.FileDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileDownloadServiceImpl implements FileDownloadService {

  private final FileDAO fileDao;
  @Value("${file.dir}")
  private String uploadDir;

  // 단일 파일 다운로드 처리
  public ResponseEntity<Resource> downloadFile(String fileId) {
    // 파일 엔티티 조회
    FileEntity fileEntity = fileDao.findById(Long.valueOf(fileId))
        .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

    // 파일 경로 설정
    Path path = Paths.get(fileEntity.getStoreFilename());
    if (!Files.exists(path)) {
      log.error("파일이 존재하지 않습니다: " + path.toString());
      throw new RuntimeException("파일이 존재하지 않습니다.");
    }
    Resource resource = new FileSystemResource(path);

    // 파일이 존재하고 읽을 수 있으면 반환
    if (resource.exists() && resource.isReadable()) {
      return createFileDownloadResponse(resource, fileEntity.getOriginalFilename());
    } else {
      throw new RuntimeException("파일을 읽을 수 없습니다.");
    }
  }

  // 파일 다운로드를 위한 ResponseEntity 생성
  private ResponseEntity<Resource> createFileDownloadResponse(Resource resource,
      String originalFilename) {
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + originalFilename + "\"")
        .body(resource);
  }

  // 결재 문서 ID로 해당 파일 정보 조회
  public List<FileDTO> loadAllFiles(String approvalFileId) {
    // 결재 문서 정보 조회
    List<FileEntity> fileEntities = fileDao.findAllByApprovalFileId(approvalFileId);
//        .orElseThrow(() -> new IllegalArgumentException("결재 문서를 찾을 수 없습니다."));

    // FileEntity를 FileDTO로 변환
    return fileEntities.stream()
        .map(file -> new FileDTO(file.getId(), file.getApprovalFileId(), file.getOriginalFilename(),
            file.getStoreFilename()))
        .collect(Collectors.toList());
  }

}
