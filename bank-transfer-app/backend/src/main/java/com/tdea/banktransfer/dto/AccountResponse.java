package com.tdea.banktransfer.dto;

import com.tdea.banktransfer.entity.Account;
import java.math.BigDecimal;

/**
 * Public view of an account.
 *
 * @param id            database id
 * @param accountNumber unique account number
 * @param ownerName     account holder
 * @param balance       current balance
 */
public record AccountResponse(Long id, String accountNumber, String ownerName, BigDecimal balance) {

    /**
     * Maps an entity to its response record.
     *
     * @param account source entity
     * @return the response DTO
     */
    public static AccountResponse from(Account account) {
        return new AccountResponse(account.getId(), account.getAccountNumber(),
                account.getOwnerName(), account.getBalance());
    }
}
