package com.bankingsystem.repository;

import com.bankingsystem.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void cleanDatabase() {
        accountRepository.deleteAll();   // ensures no duplicate data
    }

    @Test
    void saveAndFindAccount_shouldReturnCorrectData() {

        // Use a unique test account number to avoid conflicts
        Account acc = new Account("TEST12345", "Test User");
        acc.setBalance(500);

        // Save
        accountRepository.save(acc);

        // Retrieve
        Optional<Account> found = accountRepository.findByAccountNumber("TEST12345");

        // Assertions
        assertTrue(found.isPresent(), "Account should exist in DB");
        assertEquals("Test User", found.get().getHolderName());
        assertEquals(500, found.get().getBalance());
    }
}
