package com.tdea.banktransfer.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Bank account that holds a balance and can send or receive transfers. */
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Account() {
        // required by JPA
    }

    /**
     * Creates a new account.
     *
     * @param accountNumber unique account number
     * @param ownerName     name of the account holder
     * @param balance       initial balance
     */
    public Account(String accountNumber, String ownerName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Subtracts money from the balance.
     *
     * @param amount amount to withdraw, must be positive
     */
    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    /**
     * Adds money to the balance.
     *
     * @param amount amount to deposit, must be positive
     */
    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getOwnerName() { return ownerName; }
    public BigDecimal getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
