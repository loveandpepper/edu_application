package org.hofftech.edu.billing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BillingApplication {

    public static void main(String[] args) {
        System.out.println("Billing Application Started");
        SpringApplication.run(BillingApplication.class, args);
    }
}
