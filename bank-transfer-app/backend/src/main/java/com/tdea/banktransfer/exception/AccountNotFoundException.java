package com.tdea.banktransfer.exception;

/** Thrown when an account number does not exist (HTTP 404). */
public class AccountNotFoundException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param accountNumber the missing account number
     */
    public AccountNotFoundException(String accountNumber) {
        super("Account " + accountNumber + " was not found.");
    }
}
