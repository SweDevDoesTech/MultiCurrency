package dev.sweplays.multicurrency.inventories;

import com.google.common.collect.Lists;
import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Inventory_CurrencyList {

    @Getter
    private final PaginatedGui gui = Gui.paginated()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(6)
            .pageSize(45)
            .create();

    private List<String> currencyLore;

    private final List<GuiItem> currencyItems = Lists.newArrayList();

    public Inventory_CurrencyList() {
        gui.setDefaultTopClickAction(event -> {
            event.setCancelled(true);
        });

        gui.setCloseGuiAction(event -> {
            MultiCurrency.getInventoryManager().getMainInventory().openInventory((Player) event.getPlayer());
        });

        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(Utils.colorize("&b&lPrevious Page")).asGuiItem());
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(Utils.colorize("&b&lNext Page")).asGuiItem());

        int index = 0;
        for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {

            currencyLore = new ArrayList<>();
            currencyLore.add("");
            currencyLore.add(Utils.colorize("&7Name: " + currency.getName()));
            currencyLore.add(Utils.colorize("&7Symbol: " + currency.getSymbol()));
            currencyLore.add(Utils.colorize("&7Default Balance: " + currency.getDefaultBalance()));

            if (!currency.isPayable())
                currencyLore.add(Utils.colorize("&7Payable: &c" + String.valueOf(currency.isPayable()).toUpperCase()));
            else
                currencyLore.add(Utils.colorize("&7Payable: &a" + String.valueOf(currency.isPayable()).toUpperCase()));

            if (!currency.isDefault())
                currencyLore.add(Utils.colorize("&7Default: &c" + String.valueOf(currency.isDefault()).toUpperCase()));
            else
                currencyLore.add(Utils.colorize("&7Default: &a" + String.valueOf(currency.isDefault()).toUpperCase()));
            currencyLore.add(Utils.colorize("&7Material: " + currency.getInventoryMaterial()));
            currencyLore.add("");


            currencyItems.add(ItemBuilder.from(currency.getInventoryMaterial()).asGuiItem(event -> {
                gui.setCloseGuiAction(event1 -> {
                });

                SchedulerUtils.runLater(1L, () -> {
                    new Inventory_UpdateCurrency(currency).openInventory((Player) event.getWhoClicked());
                });
            }));
            ItemMeta currencyItemMeta = currencyItems.get(index).getItemStack().getItemMeta();
            currencyItemMeta.setDisplayName(Utils.colorize("&7" + currency.getName()));
            currencyItemMeta.setLore(currencyLore);
            currencyItems.get(index).getItemStack().setItemMeta(currencyItemMeta);

            gui.addItem(currencyItems.get(index));
            index++;
        }
    }

    public void openInventory(Player player) {
        gui.setCloseGuiAction(event1 -> {
            SchedulerUtils.runLater(0L, () -> {
                MultiCurrency.getInventoryManager().getMainInventory().openInventory((Player) event1.getPlayer());
            });
        });
        gui.open(player);
    }
}
