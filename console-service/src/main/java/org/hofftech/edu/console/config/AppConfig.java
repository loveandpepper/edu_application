package org.hofftech.edu.console.config;

import org.hofftech.edu.console.service.CommandParser;
import org.hofftech.edu.console.service.ShellCommandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Контекст приложения, отвечает за создание и конфигурацию зависимостей.
 * Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandParser commandParser() {
        return new CommandParser();
    }

    @Bean
    ShellCommandService shellCommandService() {
        return new ShellCommandService(restTemplate(), commandParser());
    }

}




