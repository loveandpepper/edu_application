package org.hofftech.edu.service.commandprocessor;

import org.hofftech.edu.model.ParsedCommand;

public interface CommandProcessor {
    Object execute(ParsedCommand command);
}
