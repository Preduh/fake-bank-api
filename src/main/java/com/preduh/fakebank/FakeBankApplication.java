package com.preduh.fakebank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class FakeBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakeBankApplication.class, args);
    }
}
