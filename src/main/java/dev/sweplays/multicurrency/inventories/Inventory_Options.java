package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventory_Options {

    @Getter
    private final Gui gui = Gui.gui()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(3)
            .create();

    public Inventory_Options(Object... objects) {
        if (!(objects[0] instanceof InventoryType type)) return;

        gui.setDefaultTopClickAction(event -> event.setCancelled(true));

        gui.setCloseGuiAction(event -> {
            if (type == InventoryType.SET)
                SchedulerUtils.runLater(1L, () -> new Inventory_CreateCurrency((Player) event.getPlayer()).openInventory((Player) event.getPlayer()));
            else if (type == InventoryType.UPDATE) {
                if (!(objects[1] instanceof Currency currency)) return;
                SchedulerUtils.runLater(1L, () -> new Inventory_UpdateCurrency(currency).openInventory((Player) event.getPlayer()));
            }
        });

        // Set Name
        GuiItem setNameItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {
            });
            if (type == InventoryType.SET)
                new Inventory_Set(InventoryType.SET_SINGULAR).openInventory((Player) event.getWhoClicked());
            else if (type == InventoryType.UPDATE) {
                if (!(objects[1] instanceof Currency currency)) return;
                new Inventory_Update(currency, InventoryType.UPDATE_SINGULAR).openInventory((Player) event.getWhoClicked());
            }
        });
        ItemMeta setNameItemMeta = setNameItem.getItemStack().getItemMeta();
        setNameItemMeta.setDisplayName(Utils.colorize("&b&lSet Name"));
        setNameItem.getItemStack().setItemMeta(setNameItemMeta);

        // Set Symbol
        GuiItem setSymbolItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {
            });
            if (type == InventoryType.SET)
                new Inventory_Set(InventoryType.SET_SYMBOL).openInventory((Player) event.getWhoClicked());
            else if (type == InventoryType.UPDATE) {
                if (!(objects[1] instanceof Currency currency)) return;
                new Inventory_Update(currency, InventoryType.UPDATE_SYMBOL).openInventory((Player) event.getWhoClicked());
            }
        });
        ItemMeta setSymbolItemMeta = setSymbolItem.getItemStack().getItemMeta();
        setSymbolItemMeta.setDisplayName(Utils.colorize("&b&lSet Symbol"));
        setSymbolItem.getItemStack().setItemMeta(setSymbolItemMeta);

        // Set Default Balance
        GuiItem setDefaultBalanceItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {
            });
            if (type == InventoryType.SET)
                new Inventory_Set(InventoryType.SET_DEFAULT_BALANCE).openInventory((Player) event.getWhoClicked());
            else if (type == InventoryType.UPDATE) {
                if (!(objects[1] instanceof Currency currency)) return;
                new Inventory_Update(currency, InventoryType.UPDATE_DEFAULT_BALANCE).openInventory((Player) event.getWhoClicked());
            }
        });
        ItemMeta setDefaultBalanceItemMeta = setDefaultBalanceItem.getItemStack().getItemMeta();
        setDefaultBalanceItemMeta.setDisplayName(Utils.colorize("&b&lSet Default Balance"));
        setDefaultBalanceItem.getItemStack().setItemMeta(setDefaultBalanceItemMeta);

        gui.setItem(10, setNameItem);
        gui.setItem(13, setSymbolItem);
        gui.setItem(16, setDefaultBalanceItem);
    }

    public void openInventory(Player player) {
        gui.open(player);
    }
}
