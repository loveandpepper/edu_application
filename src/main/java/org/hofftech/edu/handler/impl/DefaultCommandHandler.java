package org.hofftech.edu.handler.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.factory.commandprocessor.CommandProcessorFactory;
import org.hofftech.edu.handler.CommandHandler;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.CommandParser;

/**
 * Класс для обработки команд.
 * Определяет метод для обработки пользовательских запросов.
 */
@Slf4j
@RequiredArgsConstructor

public class DefaultCommandHandler implements CommandHandler {
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
    @Override
    public String handle(String command) throws Exception {
        ParsedCommand parsedCommand = commandParser.parse(command);
        currentProcessor = processorFactory.getProcessor(parsedCommand.getCommandType());

        if (currentProcessor != null) {
            currentProcessor.execute(parsedCommand);
            return currentProcessor.getResult();
        } else {
            throw new IllegalArgumentException("Процессор для команды не найден: " + parsedCommand.getCommandType());
        }
    }

}

