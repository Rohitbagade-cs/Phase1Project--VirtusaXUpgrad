package com.bankingsystem.dto;

import jakarta.validation.constraints.Min;


public class AmountRequest {
    @Min(value = 1, message = "amount must be > 0")
    private double amount;


    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
