package dev.sweplays.multicurrency.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.utilities.Messages;
import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mc|multicurrency|currency")
public class Command_Main extends BaseCommand {

    @Default
    public void onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length == 0 && player.hasPermission("multicurrency.command.multicurrency"))
            MultiCurrency.getInventoryManager().getMainInventory().openInventory(player);
        else if (!player.hasPermission("multicurrency.command.multicurrency")) {
            player.sendMessage(Utils.colorize(Messages.NO_PERMISSION.get()
                    .replace("{prefix}", Messages.PREFIX.get())
            ));
        }
    }
}
