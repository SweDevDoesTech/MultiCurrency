package dev.sweplays.multicurrency;

import dev.sweplays.multicurrency.account.AccountManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MultiCurrency extends JavaPlugin {

    @Getter
    private static MultiCurrency instance;

    @Getter
    private static AccountManager accountManager;

    @Override
    public void onEnable() {
        instance = this;

        accountManager = new AccountManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
