package dev.sweplays.multicurrency.api;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.account.AccountManager;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.currency.CurrencyManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class MultiCurrencyAPI {

    private final CurrencyManager currencyManager;
    private final AccountManager accountManager;

    public MultiCurrencyAPI() {
        currencyManager = MultiCurrency.getCurrencyManager();
        accountManager = MultiCurrency.getAccountManager();
    }

    /**
     * @param singular The singular name of the currency you want to create.
     * @param plural The plural name of the currency you want to create.
     * @return Created currency or null if a currency with that singular name already exists.
     */
    public Currency createCurrency(String singular, String plural) {
        if (currencyManager.currencyExists(singular)) return null;

        Currency currency = new Currency(UUID.randomUUID(), singular, plural);
        if (currencyManager.getCurrencies().size() == 0) {
            currency.setDefault(true);
        }

        currencyManager.add(currency);

        MultiCurrency.getDataStore().saveCurrency(currency);
        return currency;
    }

    /**
     * @return All loaded currencies
     */
    public List<Currency> getAllCurrencies() {
        return currencyManager.getCurrencies();
    }

    /**
     * @return Accounts of all online players
     */
    public List<Account> getAllAccounts() {
        return accountManager.getAccounts();
    }

    /**
     * @param uuid A players uuid
     * @return Players account or null if no account exists
     */
    public Account getAccount(UUID uuid) {
        for (Account account : accountManager.getAccounts()) {
            if (!account.getOwnerUuid().equals(uuid)) continue;
            return account;
        }
        return MultiCurrency.getDataStore().loadAccount(uuid);
    }

    /**
     * @apiNote Adds money with the default currency to player.
     * @param player The player you want to add money to
     * @param amount The amount of money you want to add
     */
    public void give(Player player, Double amount) {
        Account account = accountManager.getAccount(player.getUniqueId());
        if (account == null) return;
        double finalAmount = account.getBalance(currencyManager.getDefaultCurrency()) + amount;
        account.updateBalance(currencyManager.getDefaultCurrency(), finalAmount, true);
    }

    /**
     * @apiNote Adds money with the given currency to player.
     * @param player The player you want to add money to
     * @param currency The currency you want to add to a player
     * @param amount The amount of money you want to add
     */
    public void give(Player player, Currency currency, Double amount) {
        Account account = accountManager.getAccount(player.getUniqueId());
        if (account == null) return;
        double finalAmount = account.getBalance(currency) - amount;
        account.updateBalance(currency, finalAmount, true);
    }

    /**
     * @apiNote Removes money with the default currency from player.
     * @param player The player you want to remove money from
     * @param amount The amount of money you want to take
     */
    public void take(Player player, Double amount) {
        Account account = accountManager.getAccount(player.getUniqueId());
        if (account == null) return;
        double finalAmount = account.getBalance(currencyManager.getDefaultCurrency()) + amount;
        account.updateBalance(currencyManager.getDefaultCurrency(), finalAmount, true);
    }

    /**
     * @apiNote Removes money with the given currency from player.
     * @param player The player you want to remove money from
     * @param currency The currency you want to remove from a player
     * @param amount The amount of money you want to remove
     */
    public void take(Player player, Currency currency, Double amount) {
        Account account = accountManager.getAccount(player.getUniqueId());
        if (account == null) return;
        double finalAmount = account.getBalance(currency) - amount;
        account.updateBalance(currency, finalAmount, true);
    }
}
