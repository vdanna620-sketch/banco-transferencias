package com.tdea.banktransfer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tdea.banktransfer.dto.TransactionResponse;
import com.tdea.banktransfer.dto.TransferRequest;
import com.tdea.banktransfer.entity.Account;
import com.tdea.banktransfer.entity.BankTransaction;
import com.tdea.banktransfer.exception.AccountNotFoundException;
import com.tdea.banktransfer.exception.BusinessRuleException;
import com.tdea.banktransfer.exception.InsufficientFundsException;
import com.tdea.banktransfer.repository.AccountRepository;
import com.tdea.banktransfer.repository.BankTransactionRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests for the business rules of {@link TransferService} (no Spring context, no database). */
class TransferServiceTest {

    private AccountRepository accounts;
    private BankTransactionRepository transactions;
    private TransferService service;
    private Account diego;
    private Account pedro;

    @BeforeEach
    void setUp() {
        accounts = mock(AccountRepository.class);
        transactions = mock(BankTransactionRepository.class);
        service = new TransferService(accounts, transactions);
        diego = new Account("1000000001", "Diego", new BigDecimal("1000.00"));
        pedro = new Account("1000000002", "Pedro", new BigDecimal("100.00"));
        when(accounts.findByAccountNumberForUpdate("1000000001")).thenReturn(Optional.of(diego));
        when(accounts.findByAccountNumberForUpdate("1000000002")).thenReturn(Optional.of(pedro));
        when(transactions.save(any(BankTransaction.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void transferMovesMoneyAndStoresTransaction() {
        TransactionResponse result = service.transfer(
                new TransferRequest("1000000001", "1000000002", new BigDecimal("250.50"), " rent "));

        assertEquals(new BigDecimal("749.50"), diego.getBalance());
        assertEquals(new BigDecimal("350.50"), pedro.getBalance());
        assertEquals("COMPLETED", result.status());
        assertEquals("rent", result.description());
        assertNotNull(result.reference());
    }

    @Test
    void transferToSameAccountIsRejected() {
        assertThrows(BusinessRuleException.class, () -> service.transfer(
                new TransferRequest("1000000001", "1000000001", BigDecimal.TEN, null)));
    }

    @Test
    void transferWithoutFundsIsRejectedAndBalancesDoNotChange() {
        assertThrows(InsufficientFundsException.class, () -> service.transfer(
                new TransferRequest("1000000002", "1000000001", new BigDecimal("100.01"), null)));
        assertEquals(new BigDecimal("100.00"), pedro.getBalance());
        assertEquals(new BigDecimal("1000.00"), diego.getBalance());
        verify(transactions, never()).save(any());
    }

    @Test
    void transferToUnknownAccountIsRejected() {
        when(accounts.findByAccountNumberForUpdate("9999999999")).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> service.transfer(
                new TransferRequest("1000000001", "9999999999", BigDecimal.ONE, null)));
    }
}
