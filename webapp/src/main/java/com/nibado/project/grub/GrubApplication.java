package com.nibado.project.grub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrubApplication {
    public static void main(String[] args) {
        SpringApplication.run(GrubApplication.class, args);
    }
}
