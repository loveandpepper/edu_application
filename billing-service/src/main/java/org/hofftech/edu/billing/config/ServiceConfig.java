package org.hofftech.edu.billing.config;

import org.hofftech.edu.billing.service.OrderManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public OrderManagerService orderManagerService() {
        return new OrderManagerService();
    }
}
