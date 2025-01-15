package org.hofftech.edu.factory.commandprocessor;

import org.hofftech.edu.model.ParsedCommand;

public interface CommandProcessor {
    void execute(ParsedCommand command);

    /**
     * Возвращает результат выполнения команды.
     *
     * @return результат выполнения команды в виде строки
     */
    String getResult();
}
