package com.playdata.HumanResourceManagement.employee.service;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
//@ConditionalOnProperty(name = "cloud.aws.s3.enable", havingValue = "true", matchIfMissing = false)

public class EmployeeImageUploadServiceImpl implements EmployeeImageUploadService {

  private final S3Client s3Client;

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;
  @Value("${cloud.aws.region.static-region}")
  private String region;

  @Override
  public String uploadProfileImage(MultipartFile file) {
    try {
      String originalFilename = file.getOriginalFilename();
      String storeFilename = UUID.randomUUID() + "_" + originalFilename;

      PutObjectRequest putRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(storeFilename)
          .contentType(file.getContentType())
          .build();

      s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

      return storeFilename;
    } catch (IOException e) {
      throw new RuntimeException("S3 업로드 실패", e);
    }
  }

  public String getProfileImageUrl(String storedFilename) {
    return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + storedFilename;
  }
}
