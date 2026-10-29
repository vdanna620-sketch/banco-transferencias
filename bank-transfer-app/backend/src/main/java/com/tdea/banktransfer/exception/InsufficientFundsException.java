package com.tdea.banktransfer.exception;

/** Thrown when the source account does not have enough balance (HTTP 422). */
public class InsufficientFundsException extends BusinessRuleException {

    /**
     * Creates the exception.
     *
     * @param accountNumber account that lacks funds
     */
    public InsufficientFundsException(String accountNumber) {
        super("Insufficient funds in account " + accountNumber + ".");
    }
}
