package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.InsufficientBalanceException;
import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTest {

    private AccountRepository accountRepository;
    private TransactionService transactionService;
    private AccountService accountService;

    @BeforeEach
    void setup() {
        accountRepository = mock(AccountRepository.class);
        transactionService = mock(TransactionService.class);
        accountService = new AccountService(accountRepository, transactionService);

        // IMPORTANT FIX — make mock return a non-null transaction
        when(transactionService.createTransaction(any(), anyDouble(), any(), any(), any()))
                .thenReturn(new Transaction());
    }

    @Test
    void createAccount_shouldReturnSavedAccount() {
        when(accountRepository.existsByAccountNumber(anyString())).thenReturn(false);
        when(accountRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Account created = accountService.createAccount("Rohit Bagade");

        assertNotNull(created.getAccountNumber());
        assertEquals("Rohit Bagade", created.getHolderName());
    }

    @Test
    void getAccount_found_returnsAccount() {
        Account acc = new Account("RB1234", "Rohit");
        when(accountRepository.findByAccountNumber("RB1234")).thenReturn(Optional.of(acc));

        Account result = accountService.getByAccountNumber("RB1234");

        assertEquals("RB1234", result.getAccountNumber());
    }

    @Test
    void deposit_valid_updatesBalance() {
        Account acc = new Account("RB1234", "Rohit");
        acc.setBalance(200);

        when(accountRepository.findByAccountNumber("RB1234")).thenReturn(Optional.of(acc));
        when(accountRepository.save(acc)).thenReturn(acc);

        accountService.deposit("RB1234", 300);

        assertEquals(500, acc.getBalance());
    }

    @Test
    void withdraw_insufficientBalance_throwsException() {
        Account acc = new Account("RB1234", "Rohit");
        acc.setBalance(100);

        when(accountRepository.findByAccountNumber("RB1234")).thenReturn(Optional.of(acc));

        assertThrows(InsufficientBalanceException.class,
                () -> accountService.withdraw("RB1234", 200));
    }

    @Test
    void withdraw_valid_updatesBalance() {
        Account acc = new Account("RB1234", "Rohit");
        acc.setBalance(500);

        when(accountRepository.findByAccountNumber("RB1234")).thenReturn(Optional.of(acc));
        when(accountRepository.save(acc)).thenReturn(acc);

        accountService.withdraw("RB1234", 200);

        assertEquals(300, acc.getBalance());
    }
}
