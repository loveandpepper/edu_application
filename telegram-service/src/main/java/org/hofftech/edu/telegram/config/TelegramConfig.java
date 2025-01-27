package org.hofftech.edu.telegram.config;


import org.hofftech.edu.telegram.controller.TelegramBotController;
import org.hofftech.edu.telegram.service.CommandParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

/**
 * Контекст приложения, отвечает за создание и конфигурацию зависимостей.
 * Служит центральным местом для управления сервисами, контроллерами и утилитами.
 */
@Configuration
public class TelegramConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandParser commandParser() {return new CommandParser();}

    @Bean
    public TelegramBotController telegramBotController(
            @Value("${telegram.bot.token}") String token,
            @Value("${telegram.bot.name}") String botName,
            CommandParser commandParser, RestTemplate restTemplate) {
        TelegramBotController botController = new TelegramBotController(token, botName, commandParser, restTemplate);
        try {
            botController.registerBot();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка регистрации Telegram-бота", e);
        }
        return botController;
    }

}




