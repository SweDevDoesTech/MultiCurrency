package dev.sweplays.multicurrency.account;

import dev.sweplays.multicurrency.MultiCurrency;

public class AccountManager {

    private final MultiCurrency plugin;

    public AccountManager(MultiCurrency plugin) {
        this.plugin = plugin;
    }

    public boolean saveAccount() {
        return false;
    }
}
