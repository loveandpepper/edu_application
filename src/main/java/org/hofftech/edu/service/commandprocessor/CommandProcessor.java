package org.hofftech.edu.service.commandprocessor;

import org.hofftech.edu.model.ParsedCommand;

public interface CommandProcessor {
    void execute(ParsedCommand command);
}
