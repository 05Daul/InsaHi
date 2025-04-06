package com.playdata.HumanResourceManagement.employee.config;

import com.playdata.ElectronicApproval.config.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(S3Properties.class)

public class S3Config {

  private final S3Properties s3Properties;
//  @Value("${cloud.aws.credentials.access-key}")
//  private String accessKey;
//  @Value("${cloud.aws.credentials.secret-key}")
//  private String secretKey;
//  @Value("${cloud.aws.region.static}")
//  private String region;

  @Bean
  public S3Client s3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(
        s3Properties.getCredentials().getAccessKey(),
        s3Properties.getCredentials().getSecretKey()
    );
//    System.out.println("AccessKey: " + s3Properties.getCredentials().getAccessKey());
//    System.out.println("SecretKey: " + s3Properties.getCredentials().getSecretKey());
//    System.out.println("Region: " + s3Properties.getRegion().getStaticRegion());
    log.info("employee-AccessKey: {}", s3Properties.getCredentials().getAccessKey());
    log.info("employee-SecretKey: {}", s3Properties.getCredentials().getSecretKey());
    log.info("employee-Region: {}", s3Properties.getRegion().getStaticRegion());
    return S3Client.builder()
        .region(Region.of(s3Properties.getRegion().getStaticRegion()))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }
}
