package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.inventories.components.ToggleButton;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.Messages;
import dev.sweplays.multicurrency.utilities.SchedulerUtils;
import dev.sweplays.multicurrency.utilities.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Inventory_UpdateCurrency {

    private final Gui gui = Gui.gui()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(5)
            .create();

    final Currency defaultCurrency = MultiCurrency.getCurrencyManager().getDefaultCurrency();
    final AtomicReference<Material> currentMaterial;

    private final Currency currency;

    public Inventory_UpdateCurrency(Currency currency) {
        this.currency = currency;

        gui.setDefaultTopClickAction(event -> event.setCancelled(true));

        gui.setCloseGuiAction(event -> {
            if (defaultCurrency != null)
                MultiCurrency.getDataStore().saveCurrency(defaultCurrency);
            MultiCurrency.getDataStore().saveCurrency(currency);
            SchedulerUtils.runLater(1L, () -> {
                Inventory_CurrencyList inventoryCurrencyList = new Inventory_CurrencyList();
                inventoryCurrencyList.openInventory((Player) event.getPlayer());
            });
        });

        // Options
        GuiItem optionsItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {
            });

            new Inventory_Options(InventoryType.UPDATE, currency).openInventory((Player) event.getWhoClicked());
        });
        ItemMeta optionsItemMeta = optionsItem.getItemStack().getItemMeta();
        optionsItemMeta.setDisplayName(Utils.colorize("&b&lOptions"));
        optionsItem.getItemStack().setItemMeta(optionsItemMeta);

        // Payable
        List<String> payableLore = new ArrayList<>();
        payableLore.add("");
        if (!currency.isPayable())
            payableLore.add(Utils.colorize("&7Status: &cFALSE"));
        else
            payableLore.add(Utils.colorize("&7Status: &aTRUE"));
        payableLore.add("");

        ToggleButton payableToggle = new ToggleButton(20, false);
        payableToggle.setEnabled(currency.isPayable());
        GuiItem payableItem = ItemBuilder.from(Material.GOLD_INGOT).asGuiItem(event -> {
            payableToggle.toggle();

            currency.setPayable(payableToggle.isEnabled());

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

        // Default
        List<String> defaultLore = new ArrayList<>();
        defaultLore.add("");
        if (!currency.isDefault())
            defaultLore.add(Utils.colorize("&7Status: &cFALSE"));
        else
            defaultLore.add(Utils.colorize("&7Status: &aTRUE"));
        defaultLore.add("");

        ToggleButton defaultToggle = new ToggleButton(24, false);
        defaultToggle.setEnabled(currency.isDefault());
        GuiItem defaultItem = ItemBuilder.from(new ItemStack(Material.GOLD_INGOT)).asGuiItem(event -> {
            if (!defaultToggle.isEnabled()) {
                currency.setDefault(true);
                if (defaultCurrency != null)
                    defaultCurrency.setDefault(false);
                defaultToggle.toggle();
            } else {
                event.getWhoClicked().sendMessage(Utils.colorize("{prefix} &cYou must have a default currency.")
                        .replace("{prefix}", Messages.PREFIX.get())
                );
                return;
            }


            if (defaultToggle.isEnabled())
                defaultLore.set(1, Utils.colorize("&7Status: &aTRUE"));
            else
                defaultLore.set(1, Utils.colorize("&7Status: &cFALSE"));

            ItemMeta defaultItemMeta = event.getCurrentItem().getItemMeta();
            defaultItemMeta.setLore(defaultLore);
            event.getCurrentItem().setItemMeta(defaultItemMeta);
        });
        ItemMeta defaultItemMeta = defaultItem.getItemStack().getItemMeta();
        defaultItemMeta.setDisplayName(Utils.colorize("&6&lToggle Default"));
        defaultItemMeta.setLore(defaultLore);
        defaultItem.getItemStack().setItemMeta(defaultItemMeta);

        // Material
        List<String> materialLore = new ArrayList<>();
        materialLore.add("");
        materialLore.add(Utils.colorize("&7Drop an item on me to change."));
        materialLore.add("");
        currentMaterial = new AtomicReference<>(currency.getInventoryMaterial());
        GuiItem materialItem = ItemBuilder.from(currentMaterial.get()).asGuiItem(event -> {
            if (!(event.getAction() == InventoryAction.SWAP_WITH_CURSOR)) return;
            if (event.getCurrentItem() == null || event.getCursor() == null) return;
            currentMaterial.set(event.getCurrentItem().getType());
            event.getCurrentItem().setType(event.getCursor().getType());

            ItemMeta meta = event.getCurrentItem().getItemMeta();
            meta.setDisplayName(Utils.colorize("&d&lMaterial: &7" + event.getCursor().getType().toString().toUpperCase()));
            event.getCurrentItem().setItemMeta(meta);

            currency.setInventoryMaterial(event.getCursor().getType());
        });
        ItemMeta materialItemMeta = materialItem.getItemStack().getItemMeta();
        materialItemMeta.setDisplayName(Utils.colorize("&d&lMaterial: &7" + currency.getInventoryMaterial()));
        materialItemMeta.setLore(materialLore);
        materialItem.getItemStack().setItemMeta(materialItemMeta);

        // Delete
        AtomicInteger confirmCount = new AtomicInteger(0);
        GuiItem deleteItem = ItemBuilder.from(Material.BARRIER).asGuiItem(event -> {
            confirmCount.getAndIncrement();
            if (confirmCount.get() == 1) {
                event.getWhoClicked().sendMessage(Utils.colorize("{prefix} &cClick one more time to confirm deletion.")
                        .replace("{prefix}", Messages.PREFIX.get())
                );

            } else if (confirmCount.get() == 2) {
                gui.setCloseGuiAction(event1 -> {
                });
                MultiCurrency.getCurrencyManager().deleteCurrency(currency);

                for (Player player : MultiCurrency.getInstance().getServer().getOnlinePlayers())
                    MultiCurrency.getDataStore().saveAccount(MultiCurrency.getAccountManager().getAccount(player.getUniqueId()));

                if (MultiCurrency.getCurrencyManager().getCurrencies().size() > 0)
                    MultiCurrency.getCurrencyManager().getCurrencies().get(0).setDefault(true);

                SchedulerUtils.runLater(1L, () -> {
                    new Inventory_CurrencyList().openInventory((Player) event.getWhoClicked());
                });
            }
        });
        ItemMeta deleteItemMeta = deleteItem.getItemStack().getItemMeta();
        deleteItemMeta.setDisplayName(Utils.colorize("&c&lDelete Currency"));
        deleteItem.getItemStack().setItemMeta(deleteItemMeta);

        gui.setItem(13, optionsItem);
        gui.setItem(20, payableItem);
        gui.setItem(22, deleteItem);
        gui.setItem(24, defaultItem);
        gui.setItem(31, materialItem);
    }

    public void openInventory(Player player) {
        gui.setCloseGuiAction(event -> {
            if (defaultCurrency != null)
                MultiCurrency.getDataStore().saveCurrency(defaultCurrency);
            MultiCurrency.getDataStore().saveCurrency(currency);
            SchedulerUtils.runLater(1L, () -> {
                Inventory_CurrencyList inventoryCurrencyList = new Inventory_CurrencyList();
                inventoryCurrencyList.openInventory((Player) event.getPlayer());
            });
        });
        gui.open(player);
    }
}
