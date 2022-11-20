package dev.sweplays.multicurrency.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.inventories.Inventory_CurrencyList;
import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mc|multicurrency|currency")
public class Command_Main extends BaseCommand {

    private final MultiCurrency plugin;

    public Command_Main(MultiCurrency plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length == 0)
            MultiCurrency.getInventoryManager().getMainInventory().openInventory(player);

        else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            /*
            player.sendMessage(Utils.colorize("&7Currencies:"));
            if (MultiCurrency.getCurrencyManager().getCurrencies().size() == 0) {
                player.sendMessage(Utils.colorize("&cNo currencies found. Please create one."));
            }
            for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
                player.sendMessage("");
                player.sendMessage("Name: " + currency.getName());
                player.sendMessage("Uuid: " + currency.getUuid());
                player.sendMessage("Default: " + currency.isDefault());
                player.sendMessage("Payable: " + currency.isPayable());
                player.sendMessage("Default Balance: " + currency.getDefaultBalance());
            }
             */

            new Inventory_CurrencyList().openInventory(player);
            //MultiCurrency.getInventoryManager().getCurrencyListInventory().openInventory(player);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("save")) {
            for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
                MultiCurrency.getDataStore().saveCurrency(currency);
            }
            player.sendMessage(Utils.colorize("&aSuccessfully saved all currencies"));
        }
    }
}
