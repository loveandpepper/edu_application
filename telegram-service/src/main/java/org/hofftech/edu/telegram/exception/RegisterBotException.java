package org.hofftech.edu.telegram.exception;

/**
 * Исключение регистрации телеграм-бота
 */
public class RegisterBotException extends RuntimeException {

    public RegisterBotException(String message) {
        super(message);
    }
}
