package dev.sweplays.multicurrency.papi;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import org.bukkit.OfflinePlayer;
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
    public String onRequest(OfflinePlayer player, String params) {
        Currency currency = MultiCurrency.getCurrencyManager().getCurrency(params.substring(params.indexOf('_')));
        Currency defaultCurrency = MultiCurrency.getCurrencyManager().getDefaultCurrency();

        if (params.equalsIgnoreCase(""))

        if(params.equalsIgnoreCase("placeholder1")){
            return plugin.getConfig().getString("placeholders.placeholder1", "default1");
        }

        if(params.equalsIgnoreCase("placeholder2")) {
            return plugin.getConfig().getString("placeholders.placeholder2", "default2");
        }

        return null; // Placeholder is unknown by the Expansion
    }
}
