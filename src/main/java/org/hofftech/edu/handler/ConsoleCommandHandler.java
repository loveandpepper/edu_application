package org.hofftech.edu.handler;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.service.processor.command.CommandProcessor;
import org.hofftech.edu.service.processor.command.CommandProcessorFactory;
import org.hofftech.edu.model.CommandType;

@Slf4j
public class ConsoleCommandHandler implements CommandHandler {
    private final CommandProcessorFactory processorFactory;

    public ConsoleCommandHandler(CommandProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    public void handle(String command) {
        CommandType commandType = CommandType.fromCommand(command);

        CommandProcessor processor = processorFactory.getProcessor(commandType);
        if (processor != null) {
            processor.execute(command);
        } else {
            log.warn("Обработчик для команды {} не найден.", commandType);
        }
    }

}
