package com.playdata.HumanResourceManagement;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.playdata.HumanResourceManagement.repository")

public class HumanResourceManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HumanResourceManagementApplication.class, args);

    }
}
