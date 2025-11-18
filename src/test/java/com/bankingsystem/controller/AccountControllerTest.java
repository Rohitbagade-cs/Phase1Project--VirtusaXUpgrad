package com.bankingsystem.controller;

import com.bankingsystem.dto.AccountRequest;
import com.bankingsystem.model.Account;
import com.bankingsystem.service.AccountService;
import com.bankingsystem.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean   // <-- FIRST dependency
    private AccountService accountService;

    @MockBean   // <-- SECOND dependency (was missing!)
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_returnsCreatedAccount() throws Exception {

        AccountRequest req = new AccountRequest();
        req.setHolderName("Rohit");

        Account saved = new Account("RB1234", "Rohit");
        saved.setBalance(0);

        Mockito.when(accountService.createAccount("Rohit"))
                .thenReturn(saved);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.accountNumber").value("RB1234"));
    }

    @Test
    void getAccount_success() throws Exception {

        Account acc = new Account("RB1234", "Rohit");
        acc.setBalance(500);

        Mockito.when(accountService.getByAccountNumber("RB1234"))
                .thenReturn(acc);

        mockMvc.perform(get("/api/accounts/RB1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.holderName").value("Rohit"));
    }
}
