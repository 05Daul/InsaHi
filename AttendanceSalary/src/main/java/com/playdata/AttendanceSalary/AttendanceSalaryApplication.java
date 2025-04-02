package com.playdata.AttendanceSalary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableFeignClients  // Feign Client 활성화
@EnableScheduling
public class AttendanceSalaryApplication {

  public static void main(String[] args) {
    SpringApplication.run(AttendanceSalaryApplication.class, args);
  }
}


