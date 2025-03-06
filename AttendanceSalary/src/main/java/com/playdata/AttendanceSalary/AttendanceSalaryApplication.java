package com.playdata.attendanceSalary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EntityScan(basePackages = {
        "com.playdata.attendanceSalary.atdSalEntity",
        "com.playdata.Common.publicEntity",
        "com.playdata.User.employee.entity",
        "com.playdata.User.company.entity"})
@EnableJpaRepositories(basePackages = {"com.playdata.User.company.repository",
        "com.playdata.attendanceSalary.atdSalRepository.atd",
        "com.playdata.User.employee.repository"
})
@SpringBootApplication
public class AttendanceSalaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AttendanceSalaryApplication.class, args);
    }
}


