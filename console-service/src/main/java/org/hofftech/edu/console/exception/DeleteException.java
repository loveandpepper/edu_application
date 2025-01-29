package org.hofftech.edu.console.exception;

/**
 * Исключение команды удаления
 */
public class DeleteException extends RuntimeException {

    public DeleteException(String message) {
        super(message);
    }
}
