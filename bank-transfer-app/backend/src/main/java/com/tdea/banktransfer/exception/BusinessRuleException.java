package com.tdea.banktransfer.exception;

/** Thrown when a banking business rule is violated (HTTP 422). */
public class BusinessRuleException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message explanation of the violated rule
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
