package com.bankingsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
public class TransferRequest {
    @NotBlank
    private String sourceAccount;
    @NotBlank
    private String destinationAccount;
    @Min(value = 1)
    private double amount;
    public String getSourceAccount() { return sourceAccount; }
    public void setSourceAccount(String sourceAccount) { this.sourceAccount =
            sourceAccount; }
    public String getDestinationAccount() { return destinationAccount; }
    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}