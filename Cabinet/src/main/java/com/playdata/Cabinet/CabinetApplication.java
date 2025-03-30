package com.playdata.Cabinet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.playdata.Cabinet",
                "com.playdata.HumanResourceManagement"
        }
)
public class CabinetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabinetApplication.class, args);

    }
}
