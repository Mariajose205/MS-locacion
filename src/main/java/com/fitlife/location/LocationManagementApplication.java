package com.fitlife.location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = "com.fitlife.location")
@EnableJpaAuditing
public class LocationManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocationManagementApplication.class, args);
    }
}
