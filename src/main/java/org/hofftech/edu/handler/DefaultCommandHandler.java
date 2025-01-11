package org.hofftech.edu.handler;

/**
 * Интерфейс для обработки команд.
 * <p>
 * Реализации этого интерфейса обрабатывают команды, переданные в виде String
 * (парсинг команды, валидация, вызов логики, за которую отвечает команда)
 */
public interface DefaultCommandHandler {
    /**
     * Обрабатывает переданную команду.
     *
     * @param command команда для обработки в виде строки
     */
    void handle(String command);
}
