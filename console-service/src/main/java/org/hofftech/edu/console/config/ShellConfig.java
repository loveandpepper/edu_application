package org.hofftech.edu.console.config;

import lombok.RequiredArgsConstructor;
import org.hofftech.edu.console.service.CommandParser;
import org.hofftech.edu.console.service.ShellCommandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class ShellConfig {

    private final RestTemplate restTemplate;
    private final CommandParser commandParser;

    @Bean
    public ShellCommandService shellCommandService() {
        return new ShellCommandService(restTemplate, commandParser);
    }
}

