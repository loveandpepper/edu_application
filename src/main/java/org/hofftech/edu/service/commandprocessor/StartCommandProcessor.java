package org.hofftech.edu.service.commandprocessor;

import lombok.extern.slf4j.Slf4j;
import org.hofftech.edu.model.ParsedCommand;

@Slf4j
public class StartCommandProcessor implements CommandProcessor {

    @Override
    public void execute(ParsedCommand command) {
        log.info("Список команд в readme");
    }
}
