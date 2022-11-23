package dev.sweplays.multicurrency.data;

import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class YamlStorage extends DataStore {

    private YamlConfiguration configuration;
    private File file;

    public YamlStorage(String name) {
        super("yaml");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void saveAccount(Account account) {

    }

    @Override
    public void deleteAccount(Account account) {

    }

    @Override
    public void deleteBalance(Account account, Currency currency) {

    }

    @Override
    public void createAccount(Account account) {

    }

    @Override
    public void loadCurrencies() {

    }

    @Override
    public void deleteCurrency(Currency currency) {

    }

    @Override
    public List<Account> getOfflineAccounts() {
        return null;
    }

    @Override
    public void saveCurrency(Currency currency) {
    }

    @Override
    public Account loadAccount(UUID uuid) {
        return null;
    }
}
