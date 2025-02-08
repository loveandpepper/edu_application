package org.hofftech.edu.telegram.config;

import org.hofftech.edu.telegram.service.CommandParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CommandParser commandParser() {
        return new CommandParser();
    }
}

