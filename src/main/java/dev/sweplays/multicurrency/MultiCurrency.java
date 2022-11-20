package dev.sweplays.multicurrency;

import co.aikar.commands.BukkitCommandManager;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.account.AccountManager;
import dev.sweplays.multicurrency.commands.Command_Balance;
import dev.sweplays.multicurrency.commands.Command_Main;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.currency.CurrencyManager;
import dev.sweplays.multicurrency.data.DataStore;
import dev.sweplays.multicurrency.data.DatabaseManager;
import dev.sweplays.multicurrency.data.SQLiteStorage;
import dev.sweplays.multicurrency.files.FileManager;
import dev.sweplays.multicurrency.inventories.InventoryCache;
import dev.sweplays.multicurrency.inventories.InventoryManager;
import dev.sweplays.multicurrency.listeners.JoinLeaveListener;
import dev.sweplays.multicurrency.utilities.Utils;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MultiCurrency extends JavaPlugin {

    @Getter
    private static MultiCurrency instance;

    @Getter
    private static AccountManager accountManager;

    @Getter
    private static CurrencyManager currencyManager;

    @Getter
    private static FileManager fileManager;

    @Getter
    private static DatabaseManager databaseManager;

    @Getter
    private static DataStore dataStore;

    @Getter
    private static InventoryManager inventoryManager;

    @Getter
    private static InventoryCache inventoryCache;

    @Getter
    private static BukkitCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;

        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

        commandManager = new BukkitCommandManager(this);

        File dataFolder = new File(getDataFolder(), "data");
        if (!dataFolder.exists())
            dataFolder.mkdirs();

        databaseManager = new DatabaseManager(this, "data/data.db");
        databaseManager.initializeConnection();

        fileManager = new FileManager();
        currencyManager = new CurrencyManager(this);
        accountManager = new AccountManager();

        initializeDataStore("sqlite", true);

        inventoryCache = new InventoryCache();
        inventoryManager = new InventoryManager();

        commandManager.registerCommand(new Command_Balance());
        commandManager.registerCommand(new Command_Main(this));
    }

    @Override
    public void onDisable() {
        for (Player player : this.getServer().getOnlinePlayers()) {
            Account account = getAccountManager().getAccount(player.getUniqueId());

            if (account != null)
                getDataStore().saveAccount(account);
        }

        if (databaseManager != null)
            databaseManager.close();
    }

    public void initializeDataStore(String method, boolean load) {
        DataStore.getMethods().add(new SQLiteStorage());

        if (method != null)
            dataStore = DataStore.getMethod(method);
        else {
            getLogger().info(Utils.colorize("&cNo valid storage method provided."));
            getLogger().info(Utils.colorize("&cCheck the config and try again."));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            getLogger().info(Utils.colorize("Initializing datastore \"" + getDataStore().getName() + "\"..."));
            getDataStore().initialize();

            if (load) {
                getLogger().info(Utils.colorize("Loading currencies..."));
                getDataStore().loadCurrencies();
                for (Player player : this.getServer().getOnlinePlayers()) {
                    Account account = getDataStore().loadAccount(player.getUniqueId());
                    MultiCurrency.getAccountManager().addAccount(account);
                }
            }
        } catch (Throwable exception) {
            getLogger().info(Utils.colorize("&cCould not load initial data from datastore"));
            getLogger().info(Utils.colorize("&cCheck the config and try again."));
            exception.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }
}
