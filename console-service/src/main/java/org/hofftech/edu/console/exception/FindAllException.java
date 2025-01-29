package org.hofftech.edu.console.exception;

/**
 * Исключение команды отображения всех посылок
 */
public class FindAllException extends RuntimeException {

    public FindAllException(String message) {
        super(message);
    }
}
