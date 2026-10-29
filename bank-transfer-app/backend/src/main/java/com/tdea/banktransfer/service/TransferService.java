package com.tdea.banktransfer.service;

import com.tdea.banktransfer.dto.TransactionResponse;
import com.tdea.banktransfer.dto.TransferRequest;
import com.tdea.banktransfer.entity.Account;
import com.tdea.banktransfer.entity.BankTransaction;
import com.tdea.banktransfer.exception.AccountNotFoundException;
import com.tdea.banktransfer.exception.BusinessRuleException;
import com.tdea.banktransfer.exception.InsufficientFundsException;
import com.tdea.banktransfer.repository.AccountRepository;
import com.tdea.banktransfer.repository.BankTransactionRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Business logic for transferring money and querying the transaction history. */
@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final BankTransactionRepository transactionRepository;

    /**
     * Creates the service.
     *
     * @param accountRepository     account repository
     * @param transactionRepository transaction repository
     */
    public TransferService(AccountRepository accountRepository, BankTransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Moves money between two accounts atomically.
     *
     * <p>Rules: accounts must differ, both must exist and the source must have enough balance.
     * Rows are locked in a fixed (alphabetical) order to avoid deadlocks between opposite transfers.</p>
     *
     * @param request transfer data
     * @return the stored transaction
     * @throws AccountNotFoundException   when an account does not exist
     * @throws BusinessRuleException      when both accounts are the same
     * @throws InsufficientFundsException when the source balance is lower than the amount
     */
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        String sourceNumber = request.sourceAccountNumber().trim();
        String targetNumber = request.targetAccountNumber().trim();

        if (sourceNumber.equals(targetNumber)) {
            throw new BusinessRuleException("Source and target accounts must be different.");
        }

        boolean sourceFirst = sourceNumber.compareTo(targetNumber) < 0;
        Account first = lock(sourceFirst ? sourceNumber : targetNumber);
        Account second = lock(sourceFirst ? targetNumber : sourceNumber);
        Account source = sourceFirst ? first : second;
        Account target = sourceFirst ? second : first;

        if (source.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(sourceNumber);
        }

        source.debit(request.amount());
        target.credit(request.amount());

        BankTransaction saved = transactionRepository.save(new BankTransaction(
                UUID.randomUUID().toString(), source, target, request.amount(), clean(request.description())));
        return TransactionResponse.from(saved);
    }

    /**
     * Returns the transaction history, newest first.
     *
     * @param accountNumber optional filter; when blank all transactions are returned
     * @return the matching transactions
     */
    @Transactional(readOnly = true)
    public List<TransactionResponse> history(String accountNumber) {
        List<BankTransaction> result = (accountNumber == null || accountNumber.isBlank())
                ? transactionRepository.findAllWithAccounts()
                : transactionRepository.findByAccountNumber(accountNumber.trim());
        return result.stream().map(TransactionResponse::from).toList();
    }

    private Account lock(String accountNumber) {
        return accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    private String clean(String description) {
        return (description == null || description.isBlank()) ? null : description.trim();
    }
}
