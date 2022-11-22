package dev.sweplays.multicurrency.inventories;

import com.google.common.collect.Lists;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class Inventory_AccountList {

    @Getter
    private final PaginatedGui gui = Gui.paginated()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(6)
            .pageSize(45)
            .create();

    final List<GuiItem> accountItems = Lists.newArrayList();

    public Inventory_AccountList() {
        gui.setDefaultTopClickAction(event -> event.setCancelled(true));

        gui.setCloseGuiAction(event -> MultiCurrency.getInventoryManager().getMainInventory().openInventory((Player) event.getPlayer()));

        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(Utils.colorize("&b&lPrevious Page")).asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(Utils.colorize("&b&lNext Page")).asGuiItem(event -> gui.next()));

        gui.setItem(6, 5, ItemBuilder.from(Material.ANVIL).setName(Utils.colorize("&b&lSearch Player")).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {});
            new Inventory_Search().openInventory((Player) event.getWhoClicked());
        }));

        int index = 0;
        for (Account account : MultiCurrency.getAccountManager().getAccounts()) {
            if (account == null) return;
            
            List<String> accountLore = new ArrayList<>();
            accountLore.add("");
            if (account.isAcceptingPayments())
                accountLore.add(Utils.colorize("&7Payable: &a" + String.valueOf(account.isAcceptingPayments()).toUpperCase()));
            else
                accountLore.add(Utils.colorize("&7Payable: &c" + String.valueOf(account.isAcceptingPayments()).toUpperCase()));
            for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
                accountLore.add(Utils.colorize("&7" + currency.getSingular() + ": &a" + account.getBalance(currency)));
            }
            accountLore.add("");

            accountItems.add(ItemBuilder.from(Material.PLAYER_HEAD).asGuiItem(event -> {
                gui.setCloseGuiAction(event1 -> {
                });

                SchedulerUtils.runLater(1L, () -> new Inventory_UpdatePlayer(account).openInventory((Player) event.getWhoClicked()));
            }));
            SkullMeta accountItemSkullMeta = (SkullMeta) accountItems.get(index).getItemStack().getItemMeta();
            accountItemSkullMeta.setOwningPlayer(Bukkit.getPlayer(account.getOwnerUuid()));
            accountItems.get(index).getItemStack().setItemMeta(accountItemSkullMeta);

            ItemMeta accountItemMeta = accountItems.get(index).getItemStack().getItemMeta();
            accountItemMeta.setDisplayName(Utils.colorize("&7" + account.getOwnerName()));
            accountItemMeta.setLore(accountLore);
            accountItems.get(index).getItemStack().setItemMeta(accountItemMeta);

            gui.addItem(accountItems.get(index));
            index++;
        }
    }

    public void openInventory(Player player) {
        gui.setCloseGuiAction(event1 -> SchedulerUtils.runLater(0L, () -> MultiCurrency.getInventoryManager().getMainInventory().openInventory((Player) event1.getPlayer())));
        gui.open(player);
    }
}
