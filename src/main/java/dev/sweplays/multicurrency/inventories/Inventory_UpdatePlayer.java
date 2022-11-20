package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.inventories.components.ToggleButton;
import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Inventory_UpdatePlayer {

    private final Gui gui = Gui.gui()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(3)
            .create();

    private final Account account;

    public Inventory_UpdatePlayer(Account account) {
        this.account = account;

        gui.setDefaultTopClickAction(event -> {
            event.setCancelled(true);
        });

        gui.setCloseGuiAction(event -> {
            SchedulerUtils.runLater(1L, () -> {
                new Inventory_AccountList().openInventory((Player) event.getPlayer());
                MultiCurrency.getDataStore().saveAccount(account);
            });
        });

        // Payable
        List<String> payableLore = new ArrayList<>();
        payableLore.add("");
        if (!account.isAcceptingPayments())
            payableLore.add(Utils.colorize("&7Status: &cFALSE"));
        else
            payableLore.add(Utils.colorize("&7Status: &aTRUE"));
        payableLore.add("");

        ToggleButton payableToggle = new ToggleButton(20, false);
        payableToggle.setEnabled(account.isAcceptingPayments());
        GuiItem payableItem = ItemBuilder.from(Material.GOLD_INGOT).asGuiItem(event -> {
            payableToggle.toggle();

            account.setAcceptingPayments(payableToggle.isEnabled());

            if (payableToggle.isEnabled())
                payableLore.set(1, Utils.colorize("&7Status: &aTRUE"));
            else
                payableLore.set(1, Utils.colorize("&7Status: &cFALSE"));

            ItemMeta payableItemMeta = event.getCurrentItem().getItemMeta();
            payableItemMeta.setLore(payableLore);
            event.getCurrentItem().setItemMeta(payableItemMeta);
        });
        ItemMeta payableItemMeta = payableItem.getItemStack().getItemMeta();
        payableItemMeta.setDisplayName(Utils.colorize("&6&lToggle Payable"));
        payableItemMeta.setLore(payableLore);
        payableItem.getItemStack().setItemMeta(payableItemMeta);

        // Currency
        GuiItem currenciesItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {});

            new Inventory_PlayerCurrencyList(account).openInventory((Player) event.getWhoClicked());
        });
        ItemMeta currenciesItemMeta = currenciesItem.getItemStack().getItemMeta();
        currenciesItemMeta.setDisplayName(Utils.colorize("&d&lEdit Balance"));
        currenciesItem.getItemStack().setItemMeta(currenciesItemMeta);

        gui.setItem(12, payableItem);
        gui.setItem(14, currenciesItem);
    }

    public void openInventory(Player player) {
        gui.setCloseGuiAction(event -> {
            SchedulerUtils.runLater(1L, () -> {
                new Inventory_AccountList().openInventory((Player) event.getPlayer());
                MultiCurrency.getDataStore().saveAccount(account);
            });
        });
        gui.open(player);
    }
}
