package org.hofftech.edu.service.commandprocessor.impl;

import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

public class ExitCommandProcessor implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        System.exit(0);
        return "Приложение завершает работу.";
    }
}
