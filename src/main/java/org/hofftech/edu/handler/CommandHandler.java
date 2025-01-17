package org.hofftech.edu.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.factory.commandprocessor.CommandProcessorFactory;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.CommandParser;

/**
 * Класс для обработки команд.
 * Определяет метод для обработки пользовательских запросов.
 */
@Slf4j
@RequiredArgsConstructor

public class CommandHandler {

    private final CommandProcessorFactory processorFactory;
    private final CommandParser commandParser;
    @Getter
    private CommandProcessor currentProcessor;

    /**
     * Обрабатывает команду и возвращает результат выполнения.
     *
     * @param command строка команды
     * @return результат выполнения команды
     */
    public String handle(String command) {
        ParsedCommand parsedCommand = commandParser.parse(command);
        currentProcessor = processorFactory.createProcessor(parsedCommand.getCommandType());

        if (currentProcessor != null) {
            return currentProcessor.execute(parsedCommand);
        } else {
            throw new IllegalArgumentException("Процессор для команды не найден: " + parsedCommand.getCommandType());
        }
    }

}

