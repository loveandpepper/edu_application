package org.hofftech.edu.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.factory.CommandProcessorFactory;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

/**
 * Класс для обработки команд.
 * Определяет метод для обработки пользовательских запросов.
 */
@Slf4j
@RequiredArgsConstructor

public class ConsoleCommandHandler {

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
        return currentProcessor.execute(parsedCommand).toString();
    }

}

