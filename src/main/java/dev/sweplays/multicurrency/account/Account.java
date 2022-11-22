package dev.sweplays.multicurrency.account;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Account {

    @Getter
    private final UUID ownerUuid;

    @Setter
    @Getter
    private String ownerName;

    @Getter
    @Setter
    private boolean acceptingPayments = true;

    @Getter
    private final Map<Currency, Double> balances;

    public Account(UUID ownerUuid, String ownerName) {
        this.ownerUuid = ownerUuid;
        this.ownerName = ownerName;

        balances = new HashMap<>();
    }

    /*
    public void withdraw(Currency currency, Double amount) {
        if (hasEnough(currency, amount)) {
            double finalAmount = getBalance(currency) - amount;
            updateBalance(currency, finalAmount, true);
            MultiCurrency.getInstance().getLogger().info(Messages.CURRENCY_ADD_SUCCESS.get());
        }
    }
     */

    public void add(Currency currency, double amount) {
        double finalAmount = getBalance(currency) + amount;
        updateBalance(currency, finalAmount, true);
    }

    public void remove(Currency currency, double amount) {
        double finalAmount = getBalance(currency) - amount;
        updateBalance(currency, finalAmount, true);
    }


    public boolean hasEnough(Currency currency, double amount) {
        return getBalance(currency) >= amount;
    }

    public double getBalance(Currency currency) {
        return getBalances().get(currency);
    }

    public double getBalance(String name) {
        for (Currency currency : getBalances().keySet())
            if (currency.getSingular().equals(name))
                return getBalances().get(currency);
        return 0;
    }

    public void updateBalance(Currency currency, double amount, boolean save) {
        getBalances().put(currency, amount);
        if (save)
            MultiCurrency.getDataStore().saveAccount(this);
    }
}
