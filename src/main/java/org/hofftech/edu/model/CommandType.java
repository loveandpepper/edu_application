package org.hofftech.edu.model;

import lombok.Getter;

@Getter
public enum CommandType {
    IMPORT_JSON,
    SAVE,
    IMPORT,
    EXIT;

    public static CommandType fromCommand(String command) {
        if (command.contains("importjson")) {
            return IMPORT_JSON;
        } else if (command.contains("save")) {
            return SAVE;
        } else if (command.contains("file")) {
            return IMPORT;
        } else if (command.startsWith("exit")) {
            return EXIT;
        } else {
            throw new IllegalArgumentException("Неизвестный тип команды: " + command);
        }
    }
}
