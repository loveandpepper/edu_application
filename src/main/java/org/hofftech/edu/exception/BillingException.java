package org.hofftech.edu.exception;

/**
 * Исключение биллинга
 */
public class BillingException extends RuntimeException {

    public BillingException(String message) {
        super(message);
    }
}
