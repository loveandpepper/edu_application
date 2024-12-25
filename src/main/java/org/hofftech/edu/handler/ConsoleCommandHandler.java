package org.hofftech.edu.handler;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.controller.CommandProcessor;
import org.hofftech.edu.controller.CommandProcessorFactory;
import org.hofftech.edu.controller.type.CommandType;

@Slf4j
public class ConsoleCommandHandler implements CommandHandler {
    private static final String EXIT_COMMAND = "exit";

    private final CommandProcessorFactory processorFactory;

    public ConsoleCommandHandler(CommandProcessorFactory processorFactory) {
        this.processorFactory = processorFactory;
    }

    public void handle(String command) {
        if (EXIT_COMMAND.equalsIgnoreCase(command)) {
            log.info("Приложение завершает работу по команде пользователя.");
            return;
        }

        CommandType commandType = CommandType.fromCommand(command);
        if (commandType != null) {
            CommandProcessor processor = processorFactory.getProcessor(commandType);
            if (processor != null) {
                processor.process(command);
            } else {
                log.warn("Обработчик для команды {} не найден.", commandType);
            }
        } else {
            log.warn("Неизвестная команда: {}", command);
        }
    }
}
