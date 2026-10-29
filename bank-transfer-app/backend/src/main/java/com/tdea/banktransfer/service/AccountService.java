package com.tdea.banktransfer.service;

import com.tdea.banktransfer.dto.AccountResponse;
import com.tdea.banktransfer.repository.AccountRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Business logic for querying bank accounts. */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Creates the service.
     *
     * @param accountRepository account repository
     */
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Lists all accounts with their current balance.
     *
     * @return every account
     */
    @Transactional(readOnly = true)
    public List<AccountResponse> findAll() {
        return accountRepository.findAll().stream().map(AccountResponse::from).toList();
    }
}
