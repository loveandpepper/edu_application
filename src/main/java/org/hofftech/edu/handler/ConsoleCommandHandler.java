package org.hofftech.edu.handler;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.CommandParser;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;
import org.hofftech.edu.service.commandprocessor.CommandProcessorFactory;


@Slf4j
public class ConsoleCommandHandler implements CommandHandler {
    private final CommandProcessorFactory processorFactory;
    private final CommandParser commandParser;

    public ConsoleCommandHandler(CommandProcessorFactory processorFactory, CommandParser commandParser) {
        this.processorFactory = processorFactory;
        this.commandParser = commandParser;
    }

    @Override
    public void handle(String command) {
        try {
            ParsedCommand parsedCommand = commandParser.parse(command);
            CommandProcessor processor = processorFactory.getProcessor(parsedCommand);
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
