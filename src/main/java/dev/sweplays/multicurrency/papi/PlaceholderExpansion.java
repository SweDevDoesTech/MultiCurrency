package dev.sweplays.multicurrency.papi;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.Utils;
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

        } else if (params.equalsIgnoreCase("balance_default_nosymbol")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            String amount = "";
            return amount + Math.round(account.getBalance(defaultCurrency));

        } else if (params.equalsIgnoreCase("balance_default_formatted")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            String amount = "";
            return amount + defaultCurrency.getSymbol() + Utils.format(account.getBalance(defaultCurrency));

        } else if (params.equalsIgnoreCase("balance_default_formatted_nosymbol")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            String amount = "";
            return amount + Utils.format(account.getBalance(defaultCurrency));
        } else if (params.equalsIgnoreCase("balance_default_all")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            StringBuilder amount = new StringBuilder();
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                amount.append(Math.round(account1.getBalance(defaultCurrency)));
            }
            return amount.toString();

        } else if (params.equalsIgnoreCase("balance_default_all_nosymbol")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            StringBuilder amount = new StringBuilder();
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                amount.append(Math.round(account1.getBalance(defaultCurrency)));
            }
            return amount + defaultCurrency.getSymbol();

        } else if (params.equalsIgnoreCase("balance_default_all_formatted")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            StringBuilder amount = new StringBuilder();
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                amount.append(Math.round(account1.getBalance(defaultCurrency)));
            }
            return Utils.format(Double.parseDouble(amount.toString())) + defaultCurrency.getSymbol();

        } else if (params.equalsIgnoreCase("balance_default_all_formatted_nosymbol")) {
            if (defaultCurrency == null) {
                return "No default currency found.";
            }

            StringBuilder amount = new StringBuilder();
            for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                amount.append(Math.round(account1.getBalance(defaultCurrency)));
            }
            return Utils.format(Double.parseDouble(amount.toString()))
                    ;
        } else if (params.startsWith("balance_") || !params.startsWith("balance_default")) {
            String[] currencyArray = params.split("_");
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(currencyArray[1]);
            if (currency == null)
                return "No currency found.";
            if (params.equalsIgnoreCase("balance_" + currency.getSingular() + "_formatted")) {
                String amount = "";
                return amount + currency.getSymbol() + Utils.format(account.getBalance(currency));

            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular() + "_formatted_nosymbol")) {
                String amount = "";
                return amount + Utils.format(account.getBalance(currency));

            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular())) {
                String amount = "";
                return amount + currency.getSymbol() + Math.round(account.getBalance(currency));

            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular() + "_nosymbol")) {
                String amount = "";
                return amount + Math.round(account.getBalance(currency));
            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular() + "_all")) {
                StringBuilder amount = new StringBuilder();
                for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                    Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                    amount.append(Math.round(account1.getBalance(currency)));
                }
                return amount.toString();

            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular()  + "_all" + "_nosymbol")) {
                StringBuilder amount = new StringBuilder();
                for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                    Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                    amount.append(Math.round(account1.getBalance(currency)));
                }
                return amount + currency.getSymbol();

            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular() + "_all" + "_formatted")) {
                StringBuilder amount = new StringBuilder();
                for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                    Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                    amount.append(Math.round(account1.getBalance(currency)));
                }
                return Utils.format(Double.parseDouble(amount.toString())) + currency.getSymbol();

            } else if (params.equalsIgnoreCase("balance_" + currency.getSingular() + "_all" + "_formatted_nosymbol")) {
                StringBuilder amount = new StringBuilder();
                for (Player player1 : plugin.getServer().getOnlinePlayers()) {
                    Account account1 = MultiCurrency.getAccountManager().getAccount(player1.getUniqueId());
                    amount.append(Math.round(account1.getBalance(currency)));
                }
                return Utils.format(Double.parseDouble(amount.toString())) + currency.getSymbol();
            }
        }
        /*
                            Placeholders

        multicurrency_balance_default
        multicurrency_balance_default_nosymbol
        multicurrency_balance_default_formatted
        multicurrency_balance_default_formatted_nosymbol
        multicurrency_balance_default_formatted_nosymbol
        multicurrency_balance_{currency}
        multicurrency_balance_{currency}_nosymbol
        multicurrency_balance_{currency}_formatted
        multicurrency_balance_{currency}_formatted_nosymbol
         */

        return null; // Placeholder is unknown by the Expansion
    }
}
