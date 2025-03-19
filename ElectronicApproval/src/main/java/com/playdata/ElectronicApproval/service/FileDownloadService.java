package com.playdata.ElectronicApproval.service;

import com.playdata.ElectronicApproval.dto.FileDTO;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface FileDownloadService {

  ResponseEntity<Resource> downloadFile(String fileId);

  List<FileDTO> loadAllFiles(String approvalFileId);
}
