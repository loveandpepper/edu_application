package org.hofftech.edu.factory;

import org.hofftech.edu.model.CommandType;

public class CommandTypeFactory {
    public static CommandType determineCommandType(String firstArgument) {
        if (firstArgument == null || firstArgument.isEmpty()) {
            throw new RuntimeException("Первый аргумент команды пуст");
        }
        try {
            return CommandType.valueOf(firstArgument);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Неизвестная команда: " + firstArgument, e);
        }
    }
}
