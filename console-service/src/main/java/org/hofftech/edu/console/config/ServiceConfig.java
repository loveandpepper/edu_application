package org.hofftech.edu.console.config;

import org.hofftech.edu.console.service.CommandParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CommandParser commandParser() {
        return new CommandParser();
    }
}
