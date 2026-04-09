// BillingRequestDTO.java
package com.pm.billingservice.controller;

public class BillingRequestDTO {
    private String patientId;
    private String name;
    private String email;
    // getters and setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}