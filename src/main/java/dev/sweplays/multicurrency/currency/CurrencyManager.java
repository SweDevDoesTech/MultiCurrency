package dev.sweplays.multicurrency.currency;

import dev.sweplays.multicurrency.MultiCurrency;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CurrencyManager {

    @Getter
    private final List<Currency> currencies;

    public CurrencyManager() {
        this.currencies = new ArrayList<>();
    }

    public boolean currencyExists(String singular) {
        for (Currency currency : currencies)
            if (currency.getSingular().equals(singular))
                return true;
        return false;
    }

    public Currency getCurrency(UUID uuid) {
        for (Currency currency : currencies) {
            if (!currency.getUuid().equals(uuid)) continue;
            return currency;
        }
        return null;
    }

    public Currency getCurrency(String name) {
        for (Currency currency : currencies) {
            if (!currency.getId().equalsIgnoreCase(name)) continue;
            return currency;
        }
        return null;
    }

    public Currency getDefaultCurrency() {
        for (Currency currency : currencies) {
            if (currency.isDefault()) {
                return currency;
            }
        }
        return null;
    }

    public Currency createNewCurrency(String singular, String plural) {
        if (currencyExists(singular)) return null;

        Currency currency = new Currency(UUID.randomUUID(), singular, plural);
        if (currencies.size() == 0) {
            currency.setDefault(true);
        }

        add(currency);

        MultiCurrency.getDataStore().saveCurrency(currency);
        return currency;
    }

    public void deleteCurrency(Currency currency) {
        currencies.remove(currency);
        MultiCurrency.getDataStore().deleteCurrency(currency);
    }

    /**
     * @return The newly created currency.
     * @apiNote Only used for creating a currency within the plugin since it does not save after creation.
     * @since 1.0.0
     */
    public Currency createNewCurrency(String singular, String plural, String symbol, Double defaultBalance, boolean payable, Material material) {
        if (currencyExists(singular)) return null;

        Currency currency = new Currency(UUID.randomUUID(), singular, plural, symbol, defaultBalance, payable, material);

        //String[] words = currency.getSingular().split("\\s+");
        //if (words.length > 0) {

        String id = currency.getSingular().replace(" ", "-");
        currency.setId(id);

        /*
        for (int i = 0; i < words.length; i++) {

        }
        */
        //}

        if (currencies.size() == 0) {
            currency.setDefault(true);
        }

        return currency;
    }

    public void add(Currency currency) {
        if (currencies.contains(currency)) return;

        currencies.add(currency);
    }

    public void remove(Currency currency) {
        if (!currencies.contains(currency)) return;

        currencies.remove(currency);
    }
}
