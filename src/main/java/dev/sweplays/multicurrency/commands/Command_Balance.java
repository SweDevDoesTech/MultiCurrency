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

import java.util.stream.Collectors;

@CommandAlias("mc|multicurrency|currency")
public class Command_Balance extends BaseCommand {

    public Command_Balance() {
        MultiCurrency.getCommandManager().getCommandCompletions().registerAsyncCompletion("currencies", context -> {
            CommandSender sender = context.getSender();
            if (sender instanceof Player Player) {
                return MultiCurrency.getCurrencyManager().getCurrencies().stream().map(Currency::getId).collect(Collectors.toList());
            }
            return null;
        });
    }

    @Subcommand("balance|bal|money")
    @CommandCompletion("@currencies @players")
    public void onBalance(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

            if (args.length == 0 && player.hasPermission("multicurrency.command.balance")) {
                Currency currency = MultiCurrency.getCurrencyManager().getDefaultCurrency();
                if (currency == null) {
                    player.sendMessage(Utils.colorize(Messages.NO_DEFAULT_CURRENCY.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    return;
                }
                player.sendMessage(Utils.colorize(Messages.BALANCE.get(account.getBalance(currency))
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", account.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                        .replace("{amount}", String.valueOf(account.getBalance(currency)))
                        .replace("{player}", player.getName())
                ));


            } else if (args.length == 1 && player.hasPermission("multicurrency.command.balance")) {
                Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[0]);
                if (currency == null) {
                    player.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                            .replace("{currency}", args[0])
                    ));
                    return;
                }
                player.sendMessage(Utils.colorize(Messages.BALANCE.get(account.getBalance(currency))
                        .replace("{prefix}", Messages.PREFIX.get())
                        .replace("{symbol}", currency.getSymbol())
                        .replace("{currency}", account.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                        .replace("{amount}", String.valueOf(account.getBalance(currency)))
                        .replace("{player}", player.getName())
                ));
            }
        }

        if (args.length == 2 && sender.hasPermission("multicurrency.command.balance.other")) {
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[0]);
            if (currency == null) {
                sender.sendMessage(Utils.colorize(Messages.CURRENCY_NOT_FOUND.get()
                        .replace("{currency}", args[0])
                ));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", args[1])
                ));
                return;
            }
            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());
            sender.sendMessage(Utils.colorize(Messages.BALANCE_TARGET.get(targetAccount.getBalance(currency))
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", targetAccount.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(targetAccount.getBalance(currency)))
                    .replace("{target}", target.getName())
            ));
        } else if (!sender.hasPermission("multicurrency.command.balance") || !sender.hasPermission("multicurrency.command.balance.other")) {
            sender.sendMessage(Utils.colorize(Messages.NO_PERMISSION.get()
                    .replace("{prefix}", Messages.PREFIX.get())
            ));
        }
    }
}
