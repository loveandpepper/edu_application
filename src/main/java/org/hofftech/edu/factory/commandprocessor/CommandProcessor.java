package org.hofftech.edu.factory.commandprocessor;

import org.hofftech.edu.model.ParsedCommand;

public interface CommandProcessor {
    String execute(ParsedCommand command);
}
