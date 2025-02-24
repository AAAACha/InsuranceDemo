package com.schj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Descriptioin InsuranceDemoApplication
 * @Author AvA
 * @Date 2025-02-21
 */
@SpringBootApplication
@EnableTransactionManagement
public class InsuranceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(InsuranceDemoApplication.class);
    }
}
