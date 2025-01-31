package org.hofftech.edu.console.exception;

/**
 * Исключение команды поиска посылки по имени
 */
public class FindException extends RuntimeException {

    public FindException(String message) {
        super(message);
    }
}
