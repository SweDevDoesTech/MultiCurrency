package dev.sweplays.multicurrency.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.Messages;
import dev.sweplays.multicurrency.utilities.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("eco|economy")
public class Command_Economy extends BaseCommand {

    @Default
    @CommandCompletion("set|add|remove @players|@currencies @currencies 0")
    public void onEconomy(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        if (args.length == 3 && args[0].equalsIgnoreCase("set") && player.hasPermission("multicurrency.command.economy.set")) {
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[1]);
            if (currency == null) {
                player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[1])
                ));
                return;
            }
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

            account.updateBalance(currency, Double.parseDouble(args[2]), true);
            player.sendMessage(Utils.colorize(Messages.SET_SUCCESS_NO_TARGET.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[2]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[2])))
                    .replace("{player}", player.getName())
            ));

        } else if (args.length == 4 && args[0].equalsIgnoreCase("set") && player.hasPermission("multicurrency.command.economy.set")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", args[1])
                ));
                return;
            }
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[2]);
            if (currency == null) {
                player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[2])
                ));
                return;
            }
            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            targetAccount.updateBalance(currency, Double.parseDouble(args[3]), true);
            player.sendMessage(Utils.colorize(Messages.SET_SUCCESS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                            .replace("{symbol}", currency.getSymbol())
                            .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                            .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                            .replace("{target}", target.getName())
                            .replace("{player}", player.getName())
            ));
            target.sendMessage(Utils.colorize(Messages.SET_SUCCESS_TARGET.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{target}", target.getName())
                    .replace("{player}", player.getName())
            ));
        } else if (args.length == 3 && args[0].equalsIgnoreCase("add") && player.hasPermission("multicurrency.command.economy.add")) {
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[1]);
            if (currency == null) {
                player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[1])
                ));
                return;
            }
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

            double finalAmount = account.getBalance(currency) + Double.parseDouble(args[2]);

            account.updateBalance(currency, finalAmount, true);
            player.sendMessage(Utils.colorize(Messages.ADD_SUCCESS_NO_TARGET.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[2]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[2])))
                    .replace("{player}", player.getName())
            ));

        } else if (args.length == 4 && args[0].equalsIgnoreCase("add") && player.hasPermission("multicurrency.command.economy.remove")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", args[1])
                ));
                return;
            }
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[2]);
            if (currency == null) {
                player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[2])
                ));
                return;
            }
            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            double finalAmount = targetAccount.getBalance(currency) + Double.parseDouble(args[3]);

            targetAccount.updateBalance(currency, finalAmount, true);
            player.sendMessage(Utils.colorize(Messages.ADD_SUCCESS.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{target}", target.getName())
                    .replace("{player}", player.getName())
            ));
            target.sendMessage(Utils.colorize(Messages.ADD_SUCCESS_TARGET.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{target}", target.getName())
                    .replace("{player}", player.getName())
            ));

        } else if (args.length == 3 && args[0].equalsIgnoreCase("remove") && player.hasPermission("multicurrency.command.economy.remove")) {
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[1]);
            if (currency == null) {
                player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[1])
                ));
                return;
            }
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

            double finalAmount = account.getBalance(currency) - Double.parseDouble(args[2]);

            account.updateBalance(currency, finalAmount, true);
            player.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS_NO_TARGET.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[2]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[2])))
                    .replace("{player}", player.getName())
            ));
        } else if (args.length == 4 && args[0].equalsIgnoreCase("remove") && player.hasPermission("multicurrency.command.economy.remove")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", args[1])
                ));
                return;
            }
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[2]);
            if (currency == null) {
                player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[2])
                ));
                return;
            }
            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            double finalAmount = targetAccount.getBalance(currency) - Double.parseDouble(args[3]);

            targetAccount.updateBalance(currency, finalAmount, true);
            player.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{target}", target.getName())
                    .replace("{player}", player.getName())
            ));
            target.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS_TARGET.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{target}", target.getName())
                    .replace("{player}", player.getName())
            ));
        } else if (!(player.hasPermission("multicurrency.command.economy.remove") || player.hasPermission("multicurrency.command.economy.add") || player.hasPermission("multicurrency.command.economy.set"))) {
            player.sendMessage(Utils.colorize(Messages.NO_PERMISSION.get().replace("{prefix}", Messages.PREFIX.get())));
        }
    }
}
