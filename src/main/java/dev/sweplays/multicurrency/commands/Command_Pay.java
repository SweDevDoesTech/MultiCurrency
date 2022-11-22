package dev.sweplays.multicurrency.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("pay")
public class Command_Pay extends BaseCommand {

    @Default
    @CommandCompletion("@players|toggle @currencies 0")
    @CommandPermission("multicurrency.command.pay")
    public void onPay(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length == 0) {
            player.sendMessage(Utils.colorize("&cUsage: /pay <player|toggle> <currency> <amount>"));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());
            if (account == null) {
                player.sendMessage(Utils.colorize("An unknown error occurred. Please contact an admin."));
                return;
            }
            account.setAcceptingPayments(!account.isAcceptingPayments());
            if (account.isAcceptingPayments())
                player.sendMessage(Utils.colorize("&aYou can now receive currency from others."));
            else
                player.sendMessage(Utils.colorize("&cYou can no longer receive currency from others."));

        } else if (args.length == 1) {
            player.sendMessage(Utils.colorize("&cUsage: /pay <player|toggle> <currency> <amount>"));
        } else if (args.length == 2) {
            player.sendMessage(Utils.colorize("&cUsage: /pay <player|toggle> <currency> <amount>"));

        } else if (args.length == 3) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Utils.colorize("&cPlayer " + args[0] + " was not found."));
                return;
            }

            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[1]);
            if (currency == null) {
                player.sendMessage(Utils.colorize("&cCurrency " + args[1] + " does not exist."));
                return;
            }

            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());
            Account senderAccount = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());
            if (!targetAccount.isAcceptingPayments()) {
                player.sendMessage(Utils.colorize("&cPlayer " + target.getName() + " is not accepting payments at this moment."));
                return;
            }

            if (targetAccount.getBalance(currency) > 0) {
                double amount = Double.parseDouble(args[2]);

                if (amount <= 0) {
                    player.sendMessage(Utils.colorize("&cYou cannot pay a player 0" + currency.getSymbol()));
                    return;
                }

                double finalAmountSender = senderAccount.getBalance(currency) - amount;
                double finalAmountTarget = targetAccount.getBalance(currency) + amount;

                senderAccount.updateBalance(currency, finalAmountSender, true);
                targetAccount.updateBalance(currency, finalAmountTarget, true);

                player.sendMessage(Utils.colorize("&aYou sent " + amount + " to " + target.getName() + "."));
                target.sendMessage(Utils.colorize("&aYou got " + amount + " from " + player.getName() + "."));
            } else {
                player.sendMessage(Utils.colorize("&cYou don't have enough money."));
            }
        }
    }
}
