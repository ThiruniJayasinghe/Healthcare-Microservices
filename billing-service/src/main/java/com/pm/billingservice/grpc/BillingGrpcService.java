package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.pm.billingservice.model.BillingAccount;
import com.pm.billingservice.Store.BillingAccountStore;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);
    private final BillingAccountStore store;

    public BillingGrpcService(BillingAccountStore store) {
        this.store = store;
    }

    @Override
    public void createBillingAccount(BillingRequest request,
            StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received {}", request.toString());

        String accountId = UUID.randomUUID().toString();

        BillingAccount account = new BillingAccount(
                accountId,
                request.getPatientId(),
                request.getName(),
                request.getEmail(),
                "ACTIVE"
        );
        store.save(account);

        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId(accountId)
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}