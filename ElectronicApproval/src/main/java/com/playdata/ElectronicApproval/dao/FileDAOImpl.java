package com.playdata.ElectronicApproval.dao;

import com.playdata.ElectronicApproval.common.publicEntity.FileEntity;
import com.playdata.ElectronicApproval.dto.FileDTO;
import com.playdata.ElectronicApproval.repository.FileRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FileDAOImpl implements FileDAO {

  private final FileRepository fileRepository;

  // 파일 정보를 DB에 저장하는 메서드 구현 필요!
  public void saveAll(List<FileDTO> fileDTOList) {
    List<FileEntity> fileEntities = fileDTOList.stream()
        .map(dto -> FileEntity.builder()
            .approvalFileId(dto.getApprovalFileId())
            .originalFilename(dto.getOriginalFilename())
            .storeFilename(dto.getStoreFilename())
            .build())
        .collect(Collectors.toList());
    fileRepository.saveAll(fileEntities);
  }

  @Override
  public List<FileEntity> findAllByApprovalFileId(String approvalFileId) {
    return fileRepository.findAllByApprovalFileId(approvalFileId);
  }
  
  public Optional<FileEntity> findById(Long approvalFileId) {
    return fileRepository.findById(approvalFileId);
  }
}
