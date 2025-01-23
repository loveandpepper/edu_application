package org.hofftech.edu.service.commandprocessor;

import org.hofftech.edu.model.ParsedCommand;

public interface CommandProcessor {
    String execute(ParsedCommand command);
}
