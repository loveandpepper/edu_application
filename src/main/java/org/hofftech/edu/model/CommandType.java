package org.hofftech.edu.model;

import lombok.Getter;

@Getter
public enum CommandType {
    IMPORT_JSON("importjson"),
    IMPORT("import"),
    SAVE("save"),
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
