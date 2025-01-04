package org.hofftech.edu.service.commandprocessor;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.ParsedCommand;

@Slf4j
public class ExitCommandProcessor implements CommandProcessor {

    @Override
    public void execute(ParsedCommand command) {
        log.info("Приложение завершает работу.");
        System.exit(0);
    }
}
