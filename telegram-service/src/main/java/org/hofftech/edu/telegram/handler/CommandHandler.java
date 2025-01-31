package org.hofftech.edu.telegram.handler;

/**
 * Общий интерфейс для «обработчиков» телеграм-команд.
 */
public interface CommandHandler {

    /**
     * Какую команду обрабатывает этот хендлер?
     * Например, "create", "update", "delete", ...
     */
    String getCommand();

    /**
     * Логика обработки команды.
     *
     * @param input полная строка команды (например, "create param1 param2...")
     * @return результат (ответ), который нужно отправить пользователю
     */
    String handle(String input);
}
