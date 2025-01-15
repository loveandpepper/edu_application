package org.hofftech.edu.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.handler.CommandHandler;

@RequiredArgsConstructor
@Slf4j
public class TelegramController {
    private final CommandHandler commandHandler;

    /**
     * Обрабатывает команды, приходящие из Telegram.
     *
     * @param command команда от пользователя
     * @return результат выполнения команды
     */
    public String handleCommand(String command) {
        try {
            log.info("Получена команда из Telegram: {}", command);
            return commandHandler.handle(command);
        } catch (Exception e) {
            log.error("Ошибка при обработке команды: {}", e.getMessage());
            return "Ошибка: " + e.getMessage();
        }
    }
}
