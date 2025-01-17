package org.hofftech.edu.factory.commandprocessor.impl;

import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;

public class ExitCommandProcessor implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        System.exit(0);
        return "Приложение завершает работу.";
    }
}
