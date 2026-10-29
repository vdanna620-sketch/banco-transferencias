package com.tdea.banktransfer.controller;

import com.tdea.banktransfer.dto.AccountResponse;
import com.tdea.banktransfer.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST endpoint to list the bank accounts. */
@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts", description = "Query bank accounts and balances")
public class AccountController {

    private final AccountService accountService;

    /**
     * Creates the controller.
     *
     * @param accountService account service
     */
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Lists all accounts.
     *
     * @return 200 with the accounts
     */
    @GetMapping
    @Operation(summary = "List all accounts")
    public List<AccountResponse> findAll() {
        return accountService.findAll();
    }
}
