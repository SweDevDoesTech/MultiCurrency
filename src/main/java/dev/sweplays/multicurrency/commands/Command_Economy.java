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
public class Command_Economy extends BaseCommand {

    @Subcommand("eco|economy")
    @CommandCompletion("set|add|remove @players|@currencies @currencies 0")
    public void onEconomy(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 3 && args[0].equalsIgnoreCase("set") && player.hasPermission("multicurrency.command.economy.set")) {
                Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[1]);
                if (currency == null) {
                    player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                            .replace("{currency}", args[1])
                    ));
                    return;
                }

                if (!args[2].matches("[0-9]+")) {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    return;
                }

                Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

                account.updateBalance(currency, Double.parseDouble(args[2]), true);
                player.sendMessage(Utils.colorize(Messages.SET_SUCCESS_NO_TARGET.get(Double.parseDouble(args[2]))
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", Double.parseDouble(args[2]) <= 1 ? currency.getSingular() : currency.getPlural())
                        .replace("{amount}", String.valueOf(Double.parseDouble(args[2])))
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

                if (!args[2].matches("[0-9]+")) {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    return;
                }

                Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

                double finalAmount = account.getBalance(currency) + Double.parseDouble(args[2]);

                account.updateBalance(currency, finalAmount, true);
                player.sendMessage(Utils.colorize(Messages.ADD_SUCCESS_NO_TARGET.get(Double.parseDouble(args[2]))
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", Double.parseDouble(args[2]) <= 1 ? currency.getSingular() : currency.getPlural())
                        .replace("{amount}", String.valueOf(Double.parseDouble(args[2])))
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

                if (!args[2].matches("[0-9]+")) {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    return;
                }

                Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

                double finalAmount = account.getBalance(currency) - Double.parseDouble(args[2]);

                account.updateBalance(currency, finalAmount, true);
                player.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS_NO_TARGET.get(Double.parseDouble(args[2]))
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", Double.parseDouble(args[2]) <= 1 ? currency.getSingular() : currency.getPlural())
                        .replace("{amount}", String.valueOf(Double.parseDouble(args[2])))
                        .replace("{player}", player.getName())
                ));
            }
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("set") && sender.hasPermission("multicurrency.command.economy.set")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", args[1])
                ));
                return;
            }
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[2]);
            if (currency == null) {
                sender.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[2])
                ));
                return;
            }

            if (!args[3].matches("[0-9]+")) {
                sender.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                ));
                return;
            }

            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            targetAccount.updateBalance(currency, Double.parseDouble(args[3]), true);
            sender.sendMessage(Utils.colorize(Messages.SET_SUCCESS.get(Double.parseDouble(args[3]))
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{target}", target.getName())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{player}", sender.getName())
            ));

            if (!MultiCurrency.getInstance().getConfig().getBoolean("message-options.receiver-eco-messages")) return;
            target.sendMessage(Utils.colorize(Messages.SET_SUCCESS_TARGET.get(Double.parseDouble(args[3]))
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{target}", target.getName())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{player}", sender.getName())
            ));
        } else if (args.length == 4 && args[0].equalsIgnoreCase("add") && sender.hasPermission("multicurrency.command.economy.add")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", args[1])
                ));
                return;
            }
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[2]);
            if (currency == null) {
                sender.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[2])
                ));
                return;
            }

            if (!args[3].matches("[0-9]+")) {
                sender.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                ));
                return;
            }

            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            double finalAmount = targetAccount.getBalance(currency) + Double.parseDouble(args[3]);

            targetAccount.updateBalance(currency, finalAmount, true);
            sender.sendMessage(Utils.colorize(Messages.ADD_SUCCESS.get(Double.parseDouble(args[3]))
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{target}", target.getName())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{player}", sender.getName())
            ));

            if (!MultiCurrency.getInstance().getConfig().getBoolean("message-options.receiver-eco-messages")) return;
            target.sendMessage(Utils.colorize(Messages.ADD_SUCCESS_TARGET.get(Double.parseDouble(args[3]))
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{target}", target.getName())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{player}", sender.getName())
            ));

        } else if (args.length == 4 && args[0].equalsIgnoreCase("remove") && sender.hasPermission("multicurrency.command.economy.remove")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", args[1])
                ));
                return;
            }
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[2]);
            if (currency == null) {
                sender.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[2])
                ));
                return;
            }

            if (!args[3].matches("[0-9]+")) {
                sender.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                        .replace("{prefix}", Messages.PREFIX.get())
                ));
                return;
            }

            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            double finalAmount = targetAccount.getBalance(currency) - Double.parseDouble(args[3]);

            targetAccount.updateBalance(currency, finalAmount, true);
            sender.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS.get(Double.parseDouble(args[3]))
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{target}", target.getName())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{player}", sender.getName())
            ));

            if (!MultiCurrency.getInstance().getConfig().getBoolean("message-options.receiver-eco-messages")) return;
            target.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS_TARGET.get(Double.parseDouble(args[3]))
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", Double.parseDouble(args[3]) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{target}", target.getName())
                    .replace("{amount}", String.valueOf(Double.parseDouble(args[3])))
                    .replace("{player}", sender.getName())
            ));
        } else if (!(sender.hasPermission("multicurrency.command.economy.remove") || sender.hasPermission("multicurrency.command.economy.add") || sender.hasPermission("multicurrency.command.economy.set"))) {
            sender.sendMessage(Utils.colorize(Messages.NO_PERMISSION.get().replace("{prefix}", Messages.PREFIX.get())));
        }
    }
}
