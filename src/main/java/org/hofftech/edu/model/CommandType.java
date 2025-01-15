package org.hofftech.edu.model;

import lombok.Getter;

/**
 * Перечисление доступных типов команд.
 * Определяет список возможных команд для обработки в приложении.
 */
@Getter
public enum CommandType {
    START,
    EXIT,
    CREATE,
    FIND,
    UPDATE,
    DELETE,
    LIST,
    LOAD,
    UNLOAD;
}

