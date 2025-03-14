package com.playdata.AttendanceSalary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients  // Feign Client 활성화

public class AttendanceSalaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceSalaryApplication.class, args);
    }
}


