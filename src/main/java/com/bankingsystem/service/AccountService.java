package com.bankingsystem.service;

import com.bankingsystem.exception.AccountNotFoundException;
import com.bankingsystem.exception.InvalidAmountException;
import com.bankingsystem.exception.InsufficientBalanceException;
import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Random;
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final Random rand = new Random();
    public AccountService(AccountRepository accountRepository,
                          TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }
    // Create account
    public Account createAccount(String holderName) {
        if (holderName == null || holderName.trim().isEmpty()) throw new
                IllegalArgumentException("holderName required");
        String initials = buildInitials(holderName);
        String accNo;
        do {
            accNo = initials + (rand.nextInt(9000) + 1000);
        } while (accountRepository.existsByAccountNumber(accNo));
        Account a = new Account(accNo, holderName.trim());
        return accountRepository.save(a);
    }
    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFoundException("Account " +
                        accountNumber + " not found"));
    }
    public Account updateAccount(String accountNumber, Account update) {
        Account a = getByAccountNumber(accountNumber);
        if (update.getHolderName() != null)
            a.setHolderName(update.getHolderName());
        if (update.getStatus() != null) a.setStatus(update.getStatus());
        return accountRepository.save(a);
    }
    public void deleteAccount(String accountNumber) {
        Account a = getByAccountNumber(accountNumber);
        accountRepository.deleteByAccountNumber(accountNumber);
    }
    public Account deposit(String accountNumber, double amount) {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be > 0");
        Account a = getByAccountNumber(accountNumber);
        synchronized (getLockFor(a.getAccountNumber())) {
            a.setBalance(a.getBalance() + amount);
            Transaction t = transactionService.createTransaction("DEPOSIT",
                    amount, "SUCCESS", accountNumber, null);
            a.getTransactionIds().add(t.getTransactionId());
            return accountRepository.save(a);
        }
    }
    public Account withdraw(String accountNumber, double amount) {
        if (amount <= 0) throw new InvalidAmountException("Withdraw amount must be > 0");
        Account a = getByAccountNumber(accountNumber);
        synchronized (getLockFor(a.getAccountNumber())) {
            if (a.getBalance() < amount) {
                transactionService.createTransaction("WITHDRAW", amount,
                        "FAILED", accountNumber, null);
                throw new InsufficientBalanceException("Insufficient balance");
            }
            a.setBalance(a.getBalance()-amount);
            Transaction t = transactionService.createTransaction("WITHDRAW",
                    amount, "SUCCESS", accountNumber, null);
            a.getTransactionIds().add(t.getTransactionId());
            return accountRepository.save(a);
        }
    }
    public void transfer(String sourceAcc, String destAcc, double amount) {
        if (amount <= 0) throw new InvalidAmountException("Transfer amount must be > 0");
        if (sourceAcc.equals(destAcc)) throw new
                IllegalArgumentException("Source and destination must differ");
        Account src = getByAccountNumber(sourceAcc);
        Account dst = getByAccountNumber(destAcc);
        Object firstLock = getLockFor(src.getAccountNumber());
        Object secondLock = getLockFor(dst.getAccountNumber());
        // avoid deadlock: consistent order
        Object lock1 = src.getAccountNumber().compareTo(dst.getAccountNumber())
                < 0 ? firstLock : secondLock;
        Object lock2 = lock1 == firstLock ? secondLock : firstLock;
        synchronized (lock1) {
            synchronized (lock2) {
                if (src.getBalance() < amount) {
                    transactionService.createTransaction("TRANSFER", amount,
                            "FAILED", sourceAcc, destAcc);
                    throw new
                            InsufficientBalanceException("Insufficient balance for transfer");
                }
                src.setBalance(src.getBalance()-amount);
                dst.setBalance(dst.getBalance() + amount);
                Transaction t =
                        transactionService.createTransaction("TRANSFER", amount, "SUCCESS", sourceAcc,
                                destAcc);
                src.getTransactionIds().add(t.getTransactionId());
                dst.getTransactionIds().add(t.getTransactionId());
                accountRepository.save(src);
                accountRepository.save(dst);
            }
        }
    }
    public List<Account> listAll() { return accountRepository.findAll(); }
    // simple per-account lock object map (in-memory)
    private final java.util.concurrent.ConcurrentHashMap<String, Object> locks
            = new java.util.concurrent.ConcurrentHashMap<>();
    private Object getLockFor(String accNo) { return
            locks.computeIfAbsent(accNo, k-> new Object()); }
    private String buildInitials(String name) {
        return java.util.Arrays.stream(name.trim().split("\\s+"))
                .filter(s-> !s.isEmpty())
                .map(s-> s.substring(0,1).toUpperCase())
                .collect(java.util.stream.Collectors.joining());
    }
}