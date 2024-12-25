package org.hofftech.edu.controller.type;

public enum CommandType {
    IMPORT("import"),
    SAVE("save"),
    IMPORT_JSON("importjson");

    private final String prefix;

    CommandType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public static CommandType fromCommand(String command) {
        for (CommandType type : values()) {
            if (command.startsWith(type.getPrefix())) {
                return type;
            }
        }
        return null; // Можно заменить на Optional или выбросить Exception
    }
}
