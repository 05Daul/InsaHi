package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dto.ApprovalFileDTO;
import com.playdata.ElectronicApproval.dto.FileDTO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApprovalFileUploadServiceImpl implements ApprovalFileUploadService {

  @Value("${file.dir}")
  private String uploadPath;

  public String getUploadPath(String filename) {
    String dir = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
    return dir + filename;
  }


  public List<FileDTO> uploadFiles(List<MultipartFile> multipartFiles) throws IOException {
    List<FileDTO> fileDTOList = new ArrayList<>();

    for (MultipartFile multipartFile : multipartFiles) {
      if (!multipartFile.isEmpty()) {
        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);

        // 파일 저장
        multipartFile.transferTo(new File(getUploadPath(storeFilename)));

        // 저장된 파일 정보를 DTO로 저장
        fileDTOList.add(
            new FileDTO(null, null, originalFilename, storeFilename));
      }
    }

    return fileDTOList;
  }

  private String createStoreFilename(String originalFilename) {
    int position = originalFilename.lastIndexOf(".");
    String ext = originalFilename.substring(position + 1);
    String uuid = UUID.randomUUID().toString();
    return uuid + "." + ext;
  }

}
