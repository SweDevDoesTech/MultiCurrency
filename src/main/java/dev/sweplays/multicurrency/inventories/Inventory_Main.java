package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventory_Main {

    final Gui gui = Gui.gui()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(5)
            .create();

    public Inventory_Main() {
        gui.setDefaultTopClickAction(event -> event.setCancelled(true));

        // Create Currency
        GuiItem createItem = ItemBuilder.from(Material.NETHER_STAR).asGuiItem(event -> new Inventory_CreateCurrency((Player) event.getWhoClicked()).openInventory((Player) event.getWhoClicked()));
        ItemMeta createItemMeta = createItem.getItemStack().getItemMeta();
        createItemMeta.setDisplayName(Utils.colorize("&6&lCreate New Currency"));
        createItem.getItemStack().setItemMeta(createItemMeta);

        // Currency List
        GuiItem currencyListItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            event.setCancelled(true);
            SchedulerUtils.runLater(1L, () -> {
                new Inventory_CurrencyList().openInventory((Player) event.getWhoClicked());
            });
            //MultiCurrency.getInventoryManager().getCurrencyListInventory().openInventory((Player) event.getWhoClicked());
        });
        ItemMeta currencyListItemMeta = currencyListItem.getItemStack().getItemMeta();
        currencyListItemMeta.setDisplayName(Utils.colorize("&d&lCurrency List"));
        currencyListItem.getItemStack().setItemMeta(currencyListItemMeta);

        // Account List
        GuiItem accountListItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            new Inventory_AccountList().openInventory((Player) event.getWhoClicked());
            //MultiCurrency.getInventoryManager().getCurrencyListInventory().openInventory((Player) event.getWhoClicked());
        });
        ItemMeta accountListItemMeta = accountListItem.getItemStack().getItemMeta();
        accountListItemMeta.setDisplayName(Utils.colorize("&d&lAccount List"));
        accountListItem.getItemStack().setItemMeta(accountListItemMeta);

        gui.setItem(13, createItem);
        gui.setItem(20, currencyListItem);
        gui.setItem(24, accountListItem);
    }

    public void openInventory(Player player) {
        gui.open(player);
    }
}
