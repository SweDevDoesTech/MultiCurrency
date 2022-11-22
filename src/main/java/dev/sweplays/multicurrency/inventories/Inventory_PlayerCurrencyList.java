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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Inventory_PlayerCurrencyList {

    @Getter
    private final PaginatedGui gui = Gui.paginated()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(6)
            .pageSize(45)
            .create();

    private final Account account;

    public Inventory_PlayerCurrencyList(Account account) {
        this.account = account;

        gui.setDefaultTopClickAction(event -> event.setCancelled(true));

        gui.setCloseGuiAction(event -> SchedulerUtils.runLater(1L, () -> new Inventory_UpdatePlayer(account).openInventory((Player) event.getPlayer())));

        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(Utils.colorize("&b&lPrevious Page")).asGuiItem());
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(Utils.colorize("&b&lNext Page")).asGuiItem());

        List<GuiItem> currencyItems = Lists.newArrayList();

        int index = 0;
        for (Currency currency : MultiCurrency.getCurrencyManager().getCurrencies()) {
            if (account.getBalances().get(currency) == null) return;

            currencyItems.add(ItemBuilder.from(currency.getInventoryMaterial()).asGuiItem(event -> {
                gui.setCloseGuiAction(event1 -> {
                });

                SchedulerUtils.runLater(1L, () -> new Inventory_PlayerBalanceEditOptions(currency, account).openInventory((Player) event.getWhoClicked()));
            }));
            ItemMeta currencyItemMeta = currencyItems.get(index).getItemStack().getItemMeta();
            currencyItemMeta.setDisplayName(Utils.colorize("&7" + currency.getSingular()));
            currencyItems.get(index).getItemStack().setItemMeta(currencyItemMeta);

            gui.addItem(currencyItems.get(index));
            index++;
        }
    }

    public void openInventory(Player player) {
        gui.setCloseGuiAction(event -> SchedulerUtils.runLater(1L, () -> new Inventory_UpdatePlayer(account).openInventory(player)));
        gui.open(player);
    }
}
