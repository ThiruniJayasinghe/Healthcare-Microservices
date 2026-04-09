package com.pm.billingservice.model;

public class BillingAccount {
    private String accountId;
    private String patientId;
    private String name;
    private String email;
    private String status;

    public BillingAccount(String accountId, String patientId, String name, String email, String status) {
        this.accountId = accountId;
        this.patientId = patientId;
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getAccountId() { return accountId; }
    public String getPatientId() { return patientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}