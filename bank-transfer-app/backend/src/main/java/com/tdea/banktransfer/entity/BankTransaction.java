package com.tdea.banktransfer.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Immutable record of a money transfer between two accounts. */
@Entity
@Table(name = "bank_transactions")
public class BankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 36)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "source_account_id", nullable = false)
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_account_id", nullable = false)
    private Account targetAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(length = 140)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected BankTransaction() {
        // required by JPA
    }

    /**
     * Creates a completed transaction with a timestamp.
     *
     * @param reference     unique public reference (UUID)
     * @param sourceAccount account that sends the money
     * @param targetAccount account that receives the money
     * @param amount        transferred amount
     * @param description   optional free text
     */
    public BankTransaction(String reference, Account sourceAccount, Account targetAccount,
                           BigDecimal amount, String description) {
        this.reference = reference;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.description = description;
        this.status = TransactionStatus.COMPLETED;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public Account getSourceAccount() { return sourceAccount; }
    public Account getTargetAccount() { return targetAccount; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public TransactionStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
