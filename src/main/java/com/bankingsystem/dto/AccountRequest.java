package com.bankingsystem.dto;

import jakarta.validation.constraints.NotBlank;


public class AccountRequest {
    @NotBlank(message = "holderName is required")
    private String holderName;


    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }
}
