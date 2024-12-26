package org.hofftech.edu.model;

import lombok.Getter;

@Getter
public enum CommandType {
    IMPORT("import"),
    SAVE("save"),
    IMPORT_JSON("importjson"),
    EXIT("exit");

    private final String prefix;

    CommandType(String prefix) {
        this.prefix = prefix;
    }

    public static CommandType fromCommand(String command) {
        for (CommandType type : values()) {
            if (command.startsWith(type.getPrefix())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown command type: " + command);
    }
}
