package dev.sweplays.multicurrency.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mc|multicurrency|currency")
public class Command_Main extends BaseCommand {

    @Default
    @Subcommand("save")
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length == 0 && player.hasPermission("multicurrency.command.multicurrency"))
            MultiCurrency.getInventoryManager().getMainInventory().openInventory(player);

        else if (args.length == 1 && args[0].equalsIgnoreCase("save") && player.hasPermission("multicurrency.command.multicurrency.save")) {
            for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
                MultiCurrency.getDataStore().saveCurrency(currency);
            }

            for (Account account : MultiCurrency.getAccountManager().getAccounts()) {
                MultiCurrency.getDataStore().saveAccount(account);
            }
            player.sendMessage(Utils.colorize("&aSuccessfully saved all currencies and accounts."));
        }
    }
}
