package dev.sweplays.multicurrency.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.Messages;
import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("mc|multicurrency|currency")
public class Command_Pay extends BaseCommand {

    @Default
    @Subcommand("pay")
    @CommandCompletion("@players|toggle @currencies 0")
    public void onPay(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length == 0 && player.hasPermission("multicurrency.command.pay")) {
            player.sendMessage(Utils.colorize(Messages.PAY_USAGE.get()
                    .replace("{prefix}", Messages.PREFIX.get())
            ));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("toggle") && player.hasPermission("multicurrency.command.pay") && player.hasPermission("multicurrency.command.pay.toggle")) {
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());
            if (account == null) {
                player.sendMessage(Utils.colorize(Messages.ERROR.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                ));
                return;
            }
            account.setAcceptingPayments(!account.isAcceptingPayments());
            if (account.isAcceptingPayments())
                player.sendMessage(Utils.colorize(Messages.PAYMENTS_ON.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                ));
            else
                player.sendMessage(Utils.colorize(Messages.PAYMENTS_OFF.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                ));

        } else if (args.length == 1) {
            player.sendMessage(Utils.colorize(Messages.PAY_USAGE.get()
                    .replace("{prefix}", Messages.PREFIX.get())
            ));

        } else if (args.length == 2) {
            player.sendMessage(Utils.colorize(Messages.PAY_USAGE.get()
                    .replace("{prefix}", Messages.PREFIX.get())
            ));

        } else if (args.length == 3 && player.hasPermission("multicurrency.command.pay")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                player.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{target}", args[0])
                ));
                return;
            }

            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[1]);
            if (currency == null) {
                player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{currency}", args[1])
                ));
                return;
            }

            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());
            Account senderAccount = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());
            if (!targetAccount.isAcceptingPayments()) {
                player.sendMessage(Utils.colorize(Messages.TARGET_PAYMENTS_OFF.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{target}", args[0])
                ));
                return;
            }

            if (targetAccount.getBalance(currency) > 0) {
                if (!args[2].matches("[0-9]+")) {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    return;
                }

                double amount = Double.parseDouble(args[2]);

                if (amount <= 0) {
                    player.sendMessage(Utils.colorize(Messages.MINIMUM_PAYMENT.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                            .replace("{amount}", String.valueOf(0.0))
                            .replace("{symbol}", currency.getSymbol())
                    ));
                    return;
                }

                double finalAmountSender = senderAccount.getBalance(currency) - amount;
                double finalAmountTarget = targetAccount.getBalance(currency) + amount;

                senderAccount.updateBalance(currency, finalAmountSender, true);
                targetAccount.updateBalance(currency, finalAmountTarget, true);

                player.sendMessage(Utils.colorize(Messages.PAY_SUCCESS.get(amount)
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", targetAccount.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                        .replace("{amount}", String.valueOf(amount))
                        .replace("{target}", target.getName())
                ));
                target.sendMessage(Utils.colorize(Messages.PAY_SUCCESS_TARGET.get(amount)
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", targetAccount.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                        .replace("{amount}", String.valueOf(amount))
                        .replace("{player}", player.getName())
                ));
            } else {
                player.sendMessage(Utils.colorize(Messages.PAY_NOT_ENOUGH.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{currency}", currency.getPlural())
                ));
            }
        } else if (!player.hasPermission("multicurrency.command.pay") || !player.hasPermission("multicurrency.command.pay.toggle")) {
            player.sendMessage(Utils.colorize(Messages.NO_PERMISSION.get()
                    .replace("{prefix}", Messages.PREFIX.get())
            ));
        }
    }
}
