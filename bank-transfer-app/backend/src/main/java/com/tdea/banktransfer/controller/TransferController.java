package com.tdea.banktransfer.controller;

import com.tdea.banktransfer.dto.TransactionResponse;
import com.tdea.banktransfer.dto.TransferRequest;
import com.tdea.banktransfer.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/** REST endpoints for money transfers and transaction history. */
@RestController
@Tag(name = "Transfers", description = "Transfer money and query the history")
public class TransferController {

    private final TransferService transferService;

    /**
     * Creates the controller.
     *
     * @param transferService transfer service
     */
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Transfers money between two accounts.
     *
     * @param request transfer data
     * @return 201 with the transaction; 400 invalid body; 404 unknown account; 422 rule violated
     */
    @PostMapping("/transfers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Transfer money between two accounts")
    public TransactionResponse transfer(@Valid @RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }

    /**
     * Returns the transaction history.
     *
     * @param accountNumber optional account filter (sender or receiver)
     * @return 200 with the transactions, newest first
     */
    @GetMapping("/transactions")
    @Operation(summary = "Transaction history, optionally filtered by account")
    public List<TransactionResponse> history(@RequestParam(required = false) String accountNumber) {
        return transferService.history(accountNumber);
    }
}
