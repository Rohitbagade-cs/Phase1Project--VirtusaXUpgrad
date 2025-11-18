package com.bankingsystem.controller;

import com.bankingsystem.dto.*;
import com.bankingsystem.model.Account;
import com.bankingsystem.service.AccountService;
import com.bankingsystem.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);
    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse<Account>> createAccount(@Valid @RequestBody AccountRequest req) {
        Account a = accountService.createAccount(req.getHolderName());
        logger.info("Created account {} for {}", a.getAccountNumber(), a.getHolderName());
        return new ResponseEntity<>(new ApiResponse<>(201, "Account created", a), HttpStatus.CREATED);
    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<Account>> getAccount(@PathVariable String accountNumber) {
        Account a = accountService.getByAccountNumber(accountNumber);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", a));
    }
    @PutMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<Account>> updateAccount(@PathVariable String accountNumber, @RequestBody Account update) {
        Account a = accountService.updateAccount(accountNumber, update);
        return ResponseEntity.ok(new ApiResponse<>(200, "Account updated", a));
    }
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<Object>> deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse<>(200, "Account deleted", null));
    }
    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<ApiResponse<Account>> deposit(@PathVariable String accountNumber, @Valid @RequestBody AmountRequest req) {
        Account a = accountService.deposit(accountNumber, req.getAmount());
        return ResponseEntity.ok(new ApiResponse<>(200, "Deposit successful", a));
    }
    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<ApiResponse<Account>> withdraw(@PathVariable String accountNumber, @Valid @RequestBody AmountRequest req) {
        Account a = accountService.withdraw(accountNumber, req.getAmount());
        return ResponseEntity.ok(new ApiResponse<>(200, "Withdraw successful",
                a));
    }
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Object>> transfer(@Valid @RequestBody TransferRequest req) {
        accountService.transfer(req.getSourceAccount(), req.getDestinationAccount(), req.getAmount());
        return ResponseEntity.ok(new ApiResponse<>(200, "Transfer successful", null));
    }
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<ApiResponse<List<?>>> getTransactions(@PathVariable String accountNumber) {
        var list = transactionService.getTransactionsForAccount(accountNumber);
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", list));
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<Account>>> listAll() {
        return ResponseEntity.ok(new ApiResponse<>(200, "OK", accountService.listAll()));
    }
}
