package dev.sweplays.multicurrency.data;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class DataStore {

    public final MultiCurrency plugin = MultiCurrency.getInstance();

    @Getter
    private String name;

    public DataStore(String name) {
        this.name = name;
    }

    @Getter
    private static final List<DataStore> methods = new ArrayList<>();

    public static DataStore getMethod(String name) {
        for (DataStore store : getMethods()) {
            if (store.getName().equalsIgnoreCase(name)) {
                return store;
            }
        }
        return null;
    }

    public abstract void initialize();

    public abstract void saveAccount(Account account);

    public abstract void deleteAccount(Account account);

    public abstract void createAccount(Account account);

    public abstract void loadCurrencies();

    public abstract Account returnAccountWithBalances(Account account);

    public abstract List<Account> getOfflineAccounts();

    public abstract void saveCurrency(Currency currency);

    public abstract Account loadAccount(UUID uuid);

    public abstract Account loadAccount(String name);
}
