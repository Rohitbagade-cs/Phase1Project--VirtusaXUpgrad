package com.bankingsystem.service;

import com.bankingsystem.model.Transaction;
import com.bankingsystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    public Transaction createTransaction(String type, double amount, String
            status, String source, String dest) {
        String txnId = "TXN-" + Instant.now().toEpochMilli() + "-" +
                UUID.randomUUID().toString().substring(0,6);
        Transaction t = new Transaction(txnId, type, amount, status, source,
                dest);
        t.setTimestamp(Instant.now());
        return transactionRepository.save(t);
    }
    public List<Transaction> getTransactionsForAccount(String accountNumber) {
        return
                transactionRepository.findBySourceAccountOrDestinationAccount(accountNumber,
                        accountNumber);
    }
}
