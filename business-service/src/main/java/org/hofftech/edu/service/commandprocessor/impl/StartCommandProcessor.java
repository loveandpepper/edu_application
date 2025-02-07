package org.hofftech.edu.service.commandprocessor.impl;

import lombok.Getter;
import org.hofftech.edu.model.ParsedCommand;
import org.hofftech.edu.service.commandprocessor.CommandProcessor;

@Getter
public class StartCommandProcessor implements CommandProcessor {

    @Override
    public String execute(ParsedCommand command) {
        return "Список команд в readme";
    }
}
