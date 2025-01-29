package org.hofftech.edu.telegram.config;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.telegram.controller.TelegramBotController;
import org.hofftech.edu.telegram.exception.RegisterBotException;
import org.hofftech.edu.telegram.service.TelegramHandlerFactory;
import org.hofftech.edu.telegram.service.CommandParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурационный класс для телеграм-бота
 */
@Slf4j
@Configuration
public class TelegramConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandParser commandParser() {
        return new CommandParser();
    }

    /**
     * Поднимаем TelegramBotController как бин.
     * При этом передадим ему все нужные зависимости:
     * - токен, имя бота из application.properties (telegram.bot.token / telegram.bot.name),
     * - фабрику обработчиков команд (CommandHandlerFactory),
     * - регистрируем бота
     */
    @Bean
    public TelegramBotController telegramBotController(
            @Value("${telegram.bot.token}") String token,
            @Value("${telegram.bot.name}") String botName,
            TelegramHandlerFactory telegramHandlerFactory
    ) {
        TelegramBotController botController = new TelegramBotController(token, botName, telegramHandlerFactory);
        try {
            botController.registerBot();
        } catch (Exception e) {
            throw new RegisterBotException("Ошибка регистрации Telegram-бота: " + e.getMessage());
        }
        return botController;
    }
}
