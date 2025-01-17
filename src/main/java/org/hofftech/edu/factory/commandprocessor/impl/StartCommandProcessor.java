package org.hofftech.edu.factory.commandprocessor.impl;

import lombok.Getter;
import org.hofftech.edu.factory.commandprocessor.CommandProcessor;
import org.hofftech.edu.model.ParsedCommand;

@Getter
public class StartCommandProcessor implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        return "Список команд в readme";
    }
}
