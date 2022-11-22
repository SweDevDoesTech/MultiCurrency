package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventory_PlayerBalanceEditOptions {

    private final Gui gui = Gui.gui()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(3)
            .create();

    private final Account account;

    public Inventory_PlayerBalanceEditOptions(Currency currency, Account account) {
        this.account = account;

        gui.setDefaultTopClickAction(event -> event.setCancelled(true));

        gui.setCloseGuiAction(event -> SchedulerUtils.runLater(1L, () -> new Inventory_UpdatePlayer(account).openInventory((Player) event.getPlayer())));

        // Set Balance
        GuiItem setBalanceItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {});

            new Inventory_EditBalance(InventoryType.SET_BALANCE, account, currency).openInventory((Player) event.getWhoClicked());
        });
        ItemMeta setBalanceItemMeta = setBalanceItem.getItemStack().getItemMeta();
        setBalanceItemMeta.setDisplayName(Utils.colorize("&b&lSet Balance"));
        setBalanceItem.getItemStack().setItemMeta(setBalanceItemMeta);

        // Add Balance
        GuiItem addBalanceItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {});

            new Inventory_EditBalance(InventoryType.ADD_BALANCE, account, currency).openInventory((Player) event.getWhoClicked());
        });
        ItemMeta addBalanceItemMeta = addBalanceItem.getItemStack().getItemMeta();
        addBalanceItemMeta.setDisplayName(Utils.colorize("&b&lAdd Balance"));
        addBalanceItem.getItemStack().setItemMeta(addBalanceItemMeta);

        // Remove Balance
        GuiItem removeBalanceItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {});

            new Inventory_EditBalance(InventoryType.REMOVE_BALANCE, account, currency).openInventory((Player) event.getWhoClicked());
        });
        ItemMeta removeBalanceItemMeta = removeBalanceItem.getItemStack().getItemMeta();
        removeBalanceItemMeta.setDisplayName(Utils.colorize("&b&lRemove Balance"));
        removeBalanceItem.getItemStack().setItemMeta(removeBalanceItemMeta);

        gui.setItem(10, setBalanceItem);
        gui.setItem(13, addBalanceItem);
        gui.setItem(16, removeBalanceItem);
    }

    public void openInventory(Player player) {
        gui.setCloseGuiAction(event -> SchedulerUtils.runLater(1L, () -> new Inventory_UpdatePlayer(account).openInventory((Player) event.getPlayer())));
        gui.open(player);
    }
}
