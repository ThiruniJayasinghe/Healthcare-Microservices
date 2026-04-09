package com.pm.billingservice.Store;

import com.pm.billingservice.model.BillingAccount;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BillingAccountStore {
    private final Map<String, BillingAccount> store = new ConcurrentHashMap<>();

    public void save(BillingAccount account) {
        store.put(account.getAccountId(), account);
    }

    public BillingAccount findById(String accountId) {
        return store.get(accountId);
    }

    public Collection<BillingAccount> findAll() {
        return store.values();
    }

    public boolean exists(String accountId) {
        return store.containsKey(accountId);
    }

    public void delete(String accountId) {
        store.remove(accountId);
    }
}