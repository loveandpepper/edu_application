package org.hofftech.edu.exception;

/**
 * Исключение при отсутствии пользователя в аргументах
 */
public class UserNotProvidedException extends RuntimeException {

    public UserNotProvidedException(String message) {
        super(message);
    }
}
