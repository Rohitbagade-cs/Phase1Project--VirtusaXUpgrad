package com.bankingsystem.service;

import com.bankingsystem.model.Transaction;
import com.bankingsystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    private TransactionRepository transactionRepository;
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        transactionRepository = mock(TransactionRepository.class);
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    void createTransaction_shouldSaveTransaction() {
        when(transactionRepository.save(any())).thenAnswer(a -> a.getArgument(0));

        Transaction t = transactionService.createTransaction(
                "DEPOSIT", 500, "SUCCESS", "RB1234", null
        );

        assertEquals("DEPOSIT", t.getType());
        assertEquals(500, t.getAmount());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void getTransactionsForAccount_shouldReturnList() {
        when(transactionRepository.findBySourceAccountOrDestinationAccount("RB1234", "RB1234"))
                .thenReturn(List.of(new Transaction()));

        List<Transaction> result = transactionService.getTransactionsForAccount("RB1234");

        assertEquals(1, result.size());
    }
}
