package dev.sweplays.multicurrency.currency;

import com.google.common.collect.Lists;
import dev.sweplays.multicurrency.MultiCurrency;
import lombok.Getter;
import org.bukkit.Material;

import java.util.List;
import java.util.UUID;

public class CurrencyManager {

    @Getter
    private final List<Currency> currencies = Lists.newArrayList();

    public boolean currencyExists(String name) {
        for (Currency currency : currencies)
            if (currency.getSingular().equals(name))
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
            if (!currency.getSingular().equals(name)) continue;
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
     *
     * @apiNote Only used for creating a currency within the plugin since it does not save after creation.
     * @return The newly created currency.
     * @since 1.0.0
     */
    public Currency createNewCurrency(String singular, String plural, String symbol, Double defaultBalance, boolean payable, Material material) {
        if (currencyExists(singular)) return null;

        Currency currency = new Currency(UUID.randomUUID(), singular, plural, symbol, defaultBalance, payable, material);
        if (currencies.size() == 0) {
            currency.setDefault(true);
        }

        currencies.add(currency);

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

    public void replace(Currency currency) {
        if (!currencies.contains(currency)) return;

        currencies.remove(currency);
        currencies.add(currency);
    }
}
