package com.pm.billingservice.controller;

import billing.BillingRequest;
import billing.BillingResponse;
import com.pm.billingservice.grpc.BillingGrpcService;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.Store.BillingAccountStore;
import io.grpc.stub.StreamObserver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/billing")
@Tag(name = "Billing", description = "API for managing Billing Accounts")
public class BillingController {

    private final BillingGrpcService billingGrpcService;
    private final BillingAccountStore store;

    public BillingController(BillingGrpcService billingGrpcService, BillingAccountStore store) {
        this.billingGrpcService = billingGrpcService;
        this.store = store;
    }

    @GetMapping("/accounts")
    @Operation(summary = "Get all billing accounts")
    public ResponseEntity<Collection<BillingAccount>> getAllAccounts() {
        return ResponseEntity.ok(store.findAll());
    }

    @GetMapping("/accounts/{accountId}")
    @Operation(summary = "Get a billing account by ID")
    public ResponseEntity<BillingAccount> getAccount(@PathVariable String accountId) {
        BillingAccount account = store.findById(accountId);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(account);
    }

    @PostMapping("/accounts")
    @Operation(summary = "Create a billing account")
    public ResponseEntity<BillingResponseDTO> createBillingAccount(
            @RequestBody BillingRequestDTO request) {

        AtomicReference<BillingResponse> result = new AtomicReference<>();

        BillingRequest grpcRequest = BillingRequest.newBuilder()
                .setPatientId(request.getPatientId())
                .setName(request.getName())
                .setEmail(request.getEmail())
                .build();

        billingGrpcService.createBillingAccount(grpcRequest, new StreamObserver<>() {
            @Override public void onNext(BillingResponse value) { result.set(value); }
            @Override public void onError(Throwable t) {}
            @Override public void onCompleted() {}
        });

        BillingResponse response = result.get();
        return ResponseEntity.ok(new BillingResponseDTO(
                response.getAccountId(), response.getStatus()));
    }

    @PutMapping("/accounts/{accountId}")
    @Operation(summary = "Update a billing account")
    public ResponseEntity<BillingAccount> updateAccount(
            @PathVariable String accountId,
            @RequestBody BillingRequestDTO request) {

        BillingAccount account = store.findById(accountId);
        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        account.setName(request.getName());
        account.setEmail(request.getEmail());
        store.save(account);

        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/accounts/{accountId}")
    @Operation(summary = "Delete a billing account")
    public ResponseEntity<Void> deleteAccount(@PathVariable String accountId) {
        if (!store.exists(accountId)) {
            return ResponseEntity.notFound().build();
        }
        store.delete(accountId);
        return ResponseEntity.noContent().build();
    }
}