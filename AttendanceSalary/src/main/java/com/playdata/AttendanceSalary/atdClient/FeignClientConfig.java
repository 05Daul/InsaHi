package com.playdata.AttendanceSalary.atdClient;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfig {

  //3
  @Bean
  public RequestInterceptor requestInterceptor() {

    return requestTemplate -> {
      String token = extractTokenFromRequest();
      if (token != null) {
        requestTemplate.header("Authorization", "Bearer " + token);
      }
    };
  }

  private String extractTokenFromRequest() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        return authHeader.substring(7); // Bearer  제거 후 반환
      }
    }
    return null;
  }
}