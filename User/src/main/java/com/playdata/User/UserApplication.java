package com.playdata.User;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
        "com.playdata.User.employee.entity",
        "com.playdata.User.company.entity",
})
@EnableJpaRepositories(basePackages = {
        "com.playdata.User.company.repository",
        "com.playdata.User.employee.repository",
})
@SpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);

    }
}
