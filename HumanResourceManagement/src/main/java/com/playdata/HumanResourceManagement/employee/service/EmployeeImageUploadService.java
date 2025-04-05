package com.playdata.HumanResourceManagement.employee.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmployeeImageUploadService {

  String uploadProfileImage(MultipartFile file);

  String getProfileImageUrl(String storeFilename);
}
