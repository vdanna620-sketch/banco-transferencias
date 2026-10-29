package com.tdea.banktransfer.dto;

import com.tdea.banktransfer.entity.BankTransaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Public view of a transfer, also the source of the QR payload in the frontend.
 *
 * @param id                  database id
 * @param reference           public UUID reference
 * @param sourceAccountNumber debited account
 * @param sourceOwnerName     owner of the debited account
 * @param targetAccountNumber credited account
 * @param targetOwnerName     owner of the credited account
 * @param amount              transferred amount
 * @param description         optional free text
 * @param status              transfer status
 * @param createdAt           moment the transfer was made
 */
public record TransactionResponse(
        Long id, String reference,
        String sourceAccountNumber, String sourceOwnerName,
        String targetAccountNumber, String targetOwnerName,
        BigDecimal amount, String description, String status, LocalDateTime createdAt) {

    /**
     * Maps an entity (with its accounts already loaded) to its response record.
     *
     * @param tx source entity
     * @return the response DTO
     */
    public static TransactionResponse from(BankTransaction tx) {
        return new TransactionResponse(tx.getId(), tx.getReference(),
                tx.getSourceAccount().getAccountNumber(), tx.getSourceAccount().getOwnerName(),
                tx.getTargetAccount().getAccountNumber(), tx.getTargetAccount().getOwnerName(),
                tx.getAmount(), tx.getDescription(), tx.getStatus().name(), tx.getCreatedAt());
    }
}
