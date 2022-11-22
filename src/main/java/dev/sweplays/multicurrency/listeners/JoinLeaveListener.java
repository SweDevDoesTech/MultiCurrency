package dev.sweplays.multicurrency.listeners;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.utilities.Messages;
import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        SchedulerUtils.runAsync(() -> {
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

            if (account == null) {
                account = new Account(player.getUniqueId(), player.getName());

                if (MultiCurrency.getDataStore().getName().equalsIgnoreCase("sqlite")) {
                    MultiCurrency.getDataStore().createAccount(account);
                }

                MultiCurrency.getInstance().getLogger().info(Utils.colorize("New account created for: " + account.getOwnerName()));
            } else if (account.getOwnerName() == null || !account.getOwnerName().equals(player.getName())) {
                account.setOwnerName(player.getName());
                MultiCurrency.getDataStore().saveAccount(account);
                MultiCurrency.getInstance().getLogger().info("Name change detected for player " + account.getOwnerName() + ". Updating account information.");
            }
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        SchedulerUtils.run(() -> {
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());
            if (account != null) {
                MultiCurrency.getAccountManager().addAccount(account);
            }
        });

        SchedulerUtils.runLater(40L, () -> {
            if (MultiCurrency.getCurrencyManager().getDefaultCurrency() == null && (player.isOp() || player.hasPermission("multicurrency.command.currency"))) {
                player.sendMessage(Utils.colorize(Messages.PREFIX.get() + " &cYou have not made a currency yet. Please do so by typing /multicurrency."));
            }
        });
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        MultiCurrency.getInventoryCache().clearCache(player);
        MultiCurrency.getAccountManager().removeAccount(player.getUniqueId());
    }
}