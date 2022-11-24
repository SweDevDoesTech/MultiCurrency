package dev.sweplays.multicurrency.papi;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.Utils;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private final MultiCurrency plugin;

    public PlaceholderExpansion(MultiCurrency plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "multicurrency";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SweplaysYT";
    }

    @Override
    public @NotNull String getVersion() {
        return MultiCurrency.version;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null)
            return null;

        Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

        Currency defaultCurrency = MultiCurrency.getCurrencyManager().getDefaultCurrency();

        if (params.equalsIgnoreCase("balance_default")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            String amount = "";
            return amount + defaultCurrency.getSymbol() + Math.round(account.getBalance(defaultCurrency));

        } else if (params.equalsIgnoreCase("balance_default_formatted")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            String amount = "";
            return amount + defaultCurrency.getSymbol() + Utils.format(account.getBalance(defaultCurrency));

        } else if (params.startsWith("balance_") || !params.startsWith("balance_default")) {
            String[] currencyArray = params.split("_");
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(currencyArray[1]);
            if (currency == null)
                return "No currency found.";
            if (params.equalsIgnoreCase("balance_" + currency.getSingular() + "_formatted")) {
                String amount = "";
                return amount + currency.getSymbol() + Utils.format(account.getBalance(currency));
            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular())) {
                String amount = "";
                return amount + currency.getSymbol() + Math.round(account.getBalance(currency));
            }
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
