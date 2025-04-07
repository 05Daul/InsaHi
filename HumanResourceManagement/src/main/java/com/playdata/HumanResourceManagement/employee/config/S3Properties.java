package com.playdata.HumanResourceManagement.employee.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Configuration
@Data
@NoArgsConstructor
@AllArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {

  private Credentials credentials = new Credentials();
  private Region region = new Region();
  private S3 s3 = new S3();

  @Data
  public static class Credentials {

    private String accessKey;
    private String secretKey;
  }

  @Data
  public static class Region {

    private String staticRegion; // "static"은 예약어이므로 이름 바꿈
  }

  @Data
  public static class S3 {

    private String bucket;
  }
}
