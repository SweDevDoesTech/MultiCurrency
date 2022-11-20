package dev.sweplays.multicurrency.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import com.google.common.collect.ImmutableList;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("balance|bal|money")
public class Command_Balance extends BaseCommand {

    public Command_Balance() {
        MultiCurrency.getCommandManager().getCommandCompletions().registerCompletion("balance", context -> {
            for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies())
                return ImmutableList.of(currency.getName());
            return null;
        });
    }

    @Default
    public void onBalance(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) return;

        Account account = MultiCurrency.getAccountManager().getAccount(player.getUniqueId());

        if (args.length == 0) {
            Currency currency = MultiCurrency.getCurrencyManager().getDefaultCurrency();
            player.sendMessage("You have: " + account.getBalance(currency));
        } else if (args.length == 1) {
            Currency currency = MultiCurrency.getCurrencyManager().getCurrency(args[0]);
            player.sendMessage("You have: " + account.getBalance(currency));
        }
    }
}
