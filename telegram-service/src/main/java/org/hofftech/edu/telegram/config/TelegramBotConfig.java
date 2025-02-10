package org.hofftech.edu.telegram.config;

import org.hofftech.edu.telegram.controller.TelegramBotController;
import org.hofftech.edu.telegram.exception.RegisterBotException;
import org.hofftech.edu.telegram.service.TelegramHandlerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {

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

