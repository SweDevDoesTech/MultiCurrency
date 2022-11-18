package dev.sweplays.multicurrency.account;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Account {

    @Getter
    private UUID ownerUuid;

    @Getter
    private final Map<Currency, Double> balances;

    public Account(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;

        balances = new HashMap<>();
    }

    public void withdraw(Currency currency, Double amount) {
        if (hasEnough(currency, amount)) {
            double finalAmount = getBalance(currency) - amount;
            updateBalance(currency, finalAmount, true);
            MultiCurrency.getInstance().getLogger().info();
        }
    }

    public boolean hasEnough(Currency currency, Double amount) {
        return getBalance(currency) >= amount;
    }

    public Double getBalance(Currency currency) {
        return balances.get(currency);
    }

    public void updateBalance(Currency currency, double amount, boolean save) {
        balances.put(currency, amount);
        if (save)
            MultiCurrency.getAccountManager().saveAccount();
    }
}
