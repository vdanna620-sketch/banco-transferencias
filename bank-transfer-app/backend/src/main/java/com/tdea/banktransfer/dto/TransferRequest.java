package com.tdea.banktransfer.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Payload used to move money between two accounts.
 *
 * @param sourceAccountNumber account to debit
 * @param targetAccountNumber account to credit
 * @param amount              amount to transfer (at least 0.01)
 * @param description         optional free text
 */
public record TransferRequest(
        @NotBlank(message = "sourceAccountNumber is required") String sourceAccountNumber,
        @NotBlank(message = "targetAccountNumber is required") String targetAccountNumber,
        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be at least 0.01")
        @Digits(integer = 15, fraction = 2, message = "amount allows at most 2 decimals") BigDecimal amount,
        @Size(max = 140, message = "description allows at most 140 characters") String description) {
}
