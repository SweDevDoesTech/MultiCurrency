package dev.sweplays.multicurrency.account;

import com.google.common.collect.Lists;
import dev.sweplays.multicurrency.MultiCurrency;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountManager {

    @Getter
    private final List<Account> accounts;

    public AccountManager() {
        accounts = new ArrayList<>();
    }

    public Account getAccount(UUID uuid) {
        for (Account account : getAccounts()) {
            if (!account.getOwnerUuid().equals(uuid)) continue;
            return account;
        }
        return MultiCurrency.getDataStore().loadAccount(uuid);
    }

    public void removeAccount(UUID uuid) {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = getAccounts().get(i);
            if (account.getOwnerUuid().equals(uuid)) {
                accounts.remove(account);
                break;
            }
        }
    }

    public void addAccount(Account account) {
        if (accounts.contains(account)) return;

        accounts.add(account);
    }
}
