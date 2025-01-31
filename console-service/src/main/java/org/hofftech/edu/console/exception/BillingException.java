package org.hofftech.edu.console.exception;

/**
 * Исключение команды биллинга
 */
public class BillingException extends RuntimeException {

    public BillingException(String message) {
        super(message);
    }
}
