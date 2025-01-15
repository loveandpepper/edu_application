package org.hofftech.edu.factory.commandprocessor.impl;

import lombok.Getter;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;

@Getter
public class ExitCommandProcessor implements CommandProcessor {
    private String result;

    @Override
    public void execute(ParsedCommand command) {
        result = "Приложение завершает работу.";
        System.exit(0);
    }
}
