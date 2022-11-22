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

import java.util.stream.Collectors;

@CommandAlias("balance|bal|money")
public class Command_Balance extends BaseCommand {

    public Command_Balance() {
        MultiCurrency.getCommandManager().getCommandCompletions().registerAsyncCompletion("currencies", context -> {
            CommandSender sender = context.getSender();
            if (sender instanceof Player Player) {
                return MultiCurrency.getCurrencyManager().getCurrencies().stream().map(Currency::getSingular).collect(Collectors.toList());
            }
            return null;
        });
    }

    @Default
    @CommandCompletion("@currencies @players")
    public void onBalance(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

        if (args.length == 0) {
            Currency currency = MultiCurrency.getCurrencyManager().getDefaultCurrency();
            if (currency == null) {
                player.sendMessage(Utils.colorize("&cNo default currency exists. Please contact an admin."));
                return;
            }
            player.sendMessage(Utils.colorize(Messages.BALANCE.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", account.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(account.getBalance(currency)))
                    .replace("{player}", player.getName())
            ));


        } else if (args.length == 1) {
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[0]);
            if (currency == null) {
                player.sendMessage(Utils.colorize("&cCurrency " + args[0] + " does not exist."));
                return;
            }
            player.sendMessage(Utils.colorize(Messages.SET_SUCCESS.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", account.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(account.getBalance(currency)))
                    .replace("{player}", player.getName())
            ));

        } else if (args.length == 2) {
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[0]);
            if (currency == null) {
                player.sendMessage(Utils.colorize("&cCurrency " + args[0] + " does not exist."));
                return;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                player.sendMessage(Utils.colorize("&cPlayer " + args[1] + " was not found."));
                return;
            }
            Account targetAccount = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());
            player.sendMessage(Utils.colorize(Messages.BALANCE_TARGET.get()
                    .replace("{prefix}", Messages.PREFIX.get())
                    .replace("{symbol}", currency.getSymbol())
                    .replace("{currency}", targetAccount.getBalance(currency) <= 1 ? currency.getSingular() : currency.getPlural())
                    .replace("{amount}", String.valueOf(targetAccount.getBalance(currency)))
                    .replace("{target}", target.getName())
            ));
        }
    }
}
