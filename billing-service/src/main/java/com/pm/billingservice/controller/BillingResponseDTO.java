// BillingResponseDTO.java
package com.pm.billingservice.controller;

public class BillingResponseDTO {
    private String accountId;
    private String status;

    public BillingResponseDTO(String accountId, String status) {
        this.accountId = accountId;
        this.status = status;
    }
    public String getAccountId() { return accountId; }
    public String getStatus() { return status; }
}