package org.hofftech.edu.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;
import org.hofftech.edu.service.commandprocessor.CommandProcessorFactory;

/**
  Интерфейс для обработки команд.
  Определяет метод для обработки пользовательских запросов.
 */
@Slf4j
@RequiredArgsConstructor
public class CommandHandler implements DefaultCommandHandler {
    private final CommandProcessorFactory processorFactory;
    private final CommandParser commandParser;

    /**
     * Обрабатывает команду, переданную в виде строки.
     *
     * @param command строка команды
     */
    @Override
    public void handle(String command) {
        try {
            ParsedCommand parsedCommand = commandParser.parse(command);
            CommandProcessor processor = processorFactory.getProcessor(parsedCommand.getCommandType());
            if (processor != null) {
                processor.execute(parsedCommand);
            } else {
                log.error("Процессор для команды не найден: {}", parsedCommand.getCommandType());
            }
        } catch (Exception e) {
            log.error("Ошибка обработки команды: {}", e.getMessage(), e);
        }
    }
}
