package dev.sweplays.multicurrency;

import dev.sweplays.multicurrency.account.AccountManager;
import dev.sweplays.multicurrency.files.FileManager;
import dev.sweplays.multicurrency.files.MessagesFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class MultiCurrency extends JavaPlugin {

    @Getter
    private static MultiCurrency instance;

    @Getter
    private static AccountManager accountManager;

    @Getter
    private static FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;

        fileManager = new FileManager();

        accountManager = new AccountManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
