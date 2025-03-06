package com.playdata.HumanResourceManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
        "com.playdata.HumanResourceManagement.hrmEntity",
        "com.playdata.Common.publicEntity",
        "com.playdata.User.employee.entity",
        "com.playdata.User.company.entity"})
@SpringBootApplication
public class HumanResourceManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumanResourceManagementApplication.class, args);

    }
}
