package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.inventories.components.ToggleButton;
import dev.sweplays.multicurrency.utilities.InventoryType;
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

public class Inventory_CreateCurrency {

    Gui gui = Gui.gui()
            .title(Component.text(Utils.colorize("&fMulti&6Currency")))
            .rows(5)
            .create();

    private final List<String> createItemLore = new ArrayList<>();

    private final Player player;

    private final GuiItem createItem;

    public Inventory_CreateCurrency(Player player) {
        this.player = player;

        gui.setDefaultTopClickAction(event -> {
            event.setCancelled(true);
        });

        gui.setCloseGuiAction(event -> {
            SchedulerUtils.runLater(1L, () -> {
                MultiCurrency.getInventoryManager().getMainInventory().openInventory(player);
            });
        });

        // Create
        String createLoreName = MultiCurrency.getInventoryCache().getCurrencyName().get(player) == null
                ? "&7Name: UNDEFINED"
                : "&7Name: " + MultiCurrency.getInventoryCache().getCurrencyName().get(player);

        String createLoreSymbol = MultiCurrency.getInventoryCache().getSymbol().get(player) == null
                ? "&7Symbol: UNDEFINED"
                : "&7Symbol: " + MultiCurrency.getInventoryCache().getSymbol().get(player);

        String createLoreDefaultBalance = MultiCurrency.getInventoryCache().getDefaultBalance().get(player) == null
                ? "&7Default Balance: UNDEFINED"
                : "&7Default Balance: " + MultiCurrency.getInventoryCache().getDefaultBalance().get(player);

        String createLorePayable = "&7Payable: UNDEFINED";

        if (MultiCurrency.getInventoryCache().getPayable().get(player) != null) {
            if (MultiCurrency.getInventoryCache().getPayable().get(player)) {
                createLorePayable = "&7Payable: &a" + String.valueOf(MultiCurrency.getInventoryCache().getPayable().get(player)).toUpperCase();
            } else {
                createLorePayable = "&7Payable: &c" + String.valueOf(MultiCurrency.getInventoryCache().getPayable().get(player)).toUpperCase();
            }
        }

        String createLoreDefault = "&7Default: UNDEFINED";

        if (MultiCurrency.getInventoryCache().getIsDefault().get(player) != null) {
            if (MultiCurrency.getInventoryCache().getIsDefault().get(player)) {
                createLoreDefault = "&7Default: &a" + String.valueOf(MultiCurrency.getInventoryCache().getIsDefault().get(player)).toUpperCase();
            } else {
                createLoreDefault = "&7Default: &c" + String.valueOf(MultiCurrency.getInventoryCache().getIsDefault().get(player)).toUpperCase();
            }
        }

        String createLoreMaterial = MultiCurrency.getInventoryCache().getMaterial().get(player) == null
                ? "&7Material: UNDEFINED"
                : "&7Material: " + MultiCurrency.getInventoryCache().getMaterial().get(player);

        createItemLore.add(Utils.colorize(""));
        createItemLore.add(Utils.colorize(createLoreName));
        createItemLore.add(Utils.colorize(createLoreSymbol));
        createItemLore.add(Utils.colorize(createLoreDefaultBalance));
        createItemLore.add(Utils.colorize(createLorePayable));
        createItemLore.add(Utils.colorize(createLoreDefault));
        createItemLore.add(Utils.colorize(createLoreMaterial));
        createItemLore.add(Utils.colorize(""));

        createItem = ItemBuilder.from(Material.BEACON).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {});

            String name = MultiCurrency.getInventoryCache().getCurrencyName().get(player);
            String symbol = MultiCurrency.getInventoryCache().getSymbol().get(player);
            Double defaultBalance = MultiCurrency.getInventoryCache().getDefaultBalance().get(player);
            Boolean payable = MultiCurrency.getInventoryCache().getPayable().get(player);
            Boolean isDefault = MultiCurrency.getInventoryCache().getIsDefault().get(player);
            Material material = MultiCurrency.getInventoryCache().getMaterial().get(player);

            if (name == null
                    || symbol == null
                    || defaultBalance == null
                    || payable == null
                    || isDefault == null
                    || material == null) {
                player.sendMessage(Utils.colorize("&cOne or more property is not set. Hover over the beacon to check."));
                return;
            }

            Currency currency = MultiCurrency.getCurrencyManager().createNewCurrency(name, symbol, defaultBalance, payable, material);
            if (currency != null) {
                Currency currency1 = MultiCurrency.getCurrencyManager().getDefaultCurrency();
                if (currency1 != null) {
                    currency1.setDefault(false);
                    if (!currency.isDefault())
                        player.sendMessage("Removed default status from currency: " + currency1.getName() + ".");
                    MultiCurrency.getDataStore().saveCurrency(currency1);
                }
                currency.setDefault(true);
                if (currency.isDefault())
                    player.sendMessage("Set currency " + currency.getName() + " as default.");
                MultiCurrency.getCurrencyManager().add(currency);
                MultiCurrency.getDataStore().saveCurrency(currency);
                MultiCurrency.getInventoryCache().clearCache(player);

                player.sendMessage(Utils.colorize("&aSuccessfully created a new currency with name: " + name + "."));

                gui.close(player);
            } else {
                player.sendMessage(Utils.colorize("&cA currency with that name already exists. Please try again but with a different name."));
            }
        });
        ItemMeta createItemMeta = createItem.getItemStack().getItemMeta();
        createItemMeta.setDisplayName(Utils.colorize("&b&lCreate Currency"));
        createItemMeta.setLore(createItemLore);
        createItem.getItemStack().setItemMeta(createItemMeta);

        // Options
        GuiItem optionsItem = ItemBuilder.from(Material.PAPER).asGuiItem(event -> {
            gui.setCloseGuiAction(event1 -> {});

            new Inventory_Options(InventoryType.SET).openInventory((Player) event.getWhoClicked());
        });
        ItemMeta optionsItemMeta = optionsItem.getItemStack().getItemMeta();
        optionsItemMeta.setDisplayName(Utils.colorize("&b&lOptions"));
        optionsItem.getItemStack().setItemMeta(optionsItemMeta);

        // Payable
        List<String> payableLore = new ArrayList<>();
        payableLore.add("");
        if (MultiCurrency.getInventoryCache().getPayable().get(player) == null || !MultiCurrency.getInventoryCache().getPayable().get(player))
            payableLore.add(Utils.colorize("&7Status: &cFALSE"));
        else
            payableLore.add(Utils.colorize("&7Status: &aTRUE"));
        payableLore.add("");

        ToggleButton payableToggle = new ToggleButton(20, false);
        if (MultiCurrency.getInventoryCache().getPayable().get(player) != null)
            payableToggle.setEnabled(MultiCurrency.getInventoryCache().getPayable().get(player));
        GuiItem payableItem = ItemBuilder.from(Material.GOLD_INGOT).asGuiItem(event -> {
            payableToggle.toggle();

            MultiCurrency.getInventoryCache().getPayable().put((Player) event.getWhoClicked(), payableToggle.isEnabled());

            if (payableToggle.isEnabled())
                payableLore.set(1, Utils.colorize("&7Status: &aTRUE"));
            else
                payableLore.set(1, Utils.colorize("&7Status: &cFALSE"));

            ItemMeta payableItemMeta = event.getCurrentItem().getItemMeta();
            payableItemMeta.setLore(payableLore);
            event.getCurrentItem().setItemMeta(payableItemMeta);

            updateCreationLore();
        });
        ItemMeta payableItemMeta = payableItem.getItemStack().getItemMeta();
        payableItemMeta.setDisplayName(Utils.colorize("&6&lToggle Payable"));
        payableItemMeta.setLore(payableLore);
        payableItem.getItemStack().setItemMeta(payableItemMeta);

        // Default
        List<String> defaultLore = new ArrayList<>();
        defaultLore.add("");
        if (MultiCurrency.getInventoryCache().getIsDefault().get(player) == null || !MultiCurrency.getInventoryCache().getIsDefault().get(player))
            defaultLore.add(Utils.colorize("&7Status: &cFALSE"));
        else
            defaultLore.add(Utils.colorize("&7Status: &aTRUE"));
        defaultLore.add("");

        ToggleButton defaultToggle = new ToggleButton(24, false);
        if (MultiCurrency.getInventoryCache().getIsDefault().get(player) != null)
            defaultToggle.setEnabled(MultiCurrency.getInventoryCache().getIsDefault().get(player));
        GuiItem defaultItem = ItemBuilder.from(new ItemStack(Material.GOLD_INGOT)).asGuiItem(event -> {
            defaultToggle.toggle();

            MultiCurrency.getInventoryCache().getIsDefault().put((Player) event.getWhoClicked(), defaultToggle.isEnabled());

            if (defaultToggle.isEnabled())
                defaultLore.set(1, Utils.colorize("&7Status: &aTRUE"));
            else
                defaultLore.set(1, Utils.colorize("&7Status: &cFALSE"));

            ItemMeta defaultItemMeta = event.getCurrentItem().getItemMeta();
            defaultItemMeta.setLore(defaultLore);
            event.getCurrentItem().setItemMeta(defaultItemMeta);

            updateCreationLore();
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

        Material currentMaterial;
        if (MultiCurrency.getInventoryCache().getMaterial().get(player) != null)
            currentMaterial = MultiCurrency.getInventoryCache().getMaterial().get(player);
        else
            currentMaterial = Material.BARRIER;

        GuiItem materialItem = ItemBuilder.from(currentMaterial).asGuiItem(event -> {

            if (!(event.getAction() == InventoryAction.SWAP_WITH_CURSOR)) return;
            event.getCurrentItem().setType(event.getCursor().getType());

            ItemMeta meta = event.getCurrentItem().getItemMeta();
            meta.setDisplayName(Utils.colorize("&d&lMaterial: &7" + event.getCursor().getType().toString().toUpperCase()));
            event.getCurrentItem().setItemMeta(meta);

            MultiCurrency.getInventoryCache().getMaterial().put((Player) event.getWhoClicked(), event.getCursor().getType());

            updateCreationLore();
        });
        ItemMeta materialItemMeta = materialItem.getItemStack().getItemMeta();
        if (MultiCurrency.getInventoryCache().getMaterial().get(player) == null)
            materialItemMeta.setDisplayName(Utils.colorize("&d&lMaterial: &7BARRIER"));
        else
            materialItemMeta.setDisplayName(Utils.colorize("&d&lMaterial: &7" + MultiCurrency.getInventoryCache().getMaterial().get(player)));
        materialItemMeta.setLore(materialLore);
        materialItem.getItemStack().setItemMeta(materialItemMeta);

        gui.setItem(22, createItem);
        gui.setItem(13, optionsItem);
        gui.setItem(20, payableItem);
        gui.setItem(24, defaultItem);
        gui.setItem(31, materialItem);
    }

    public void openInventory(Player player) {
        gui.setCloseGuiAction(event -> {
            SchedulerUtils.runLater(1L, () -> {
                MultiCurrency.getInventoryManager().getMainInventory().openInventory(player);
            });
        });
        gui.open(player);
    }

    public void updateCreationLore() {
        if (MultiCurrency.getInventoryCache().getCurrencyName().get(player) != null)
            createItemLore.set(1, Utils.colorize("&7Name: " + MultiCurrency.getInventoryCache().getCurrencyName().get(player)));

        if (MultiCurrency.getInventoryCache().getSymbol().get(player) != null)
            createItemLore.set(2, Utils.colorize("&7Symbol: " + MultiCurrency.getInventoryCache().getSymbol().get(player)));

        if (MultiCurrency.getInventoryCache().getDefaultBalance().get(player) != null)
            createItemLore.set(3, Utils.colorize("&7Default Balance: " + MultiCurrency.getInventoryCache().getDefaultBalance().get(player)));

        if (MultiCurrency.getInventoryCache().getPayable().get(player) != null)
            if (!MultiCurrency.getInventoryCache().getPayable().get(player))
                createItemLore.set(4, Utils.colorize("&7Payable: &c" + String.valueOf(MultiCurrency.getInventoryCache().getPayable().get(player)).toUpperCase()));
            else
                createItemLore.set(4, Utils.colorize("&7Payable: &a" + String.valueOf(MultiCurrency.getInventoryCache().getPayable().get(player)).toUpperCase()));

        if (MultiCurrency.getInventoryCache().getIsDefault().get(player) != null)
            if (!MultiCurrency.getInventoryCache().getIsDefault().get(player))
                createItemLore.set(5, Utils.colorize("&7Default: &c" + String.valueOf(MultiCurrency.getInventoryCache().getIsDefault().get(player)).toUpperCase()));
            else
                createItemLore.set(5, Utils.colorize("&7Default: &a" + String.valueOf(MultiCurrency.getInventoryCache().getIsDefault().get(player)).toUpperCase()));

        if (MultiCurrency.getInventoryCache().getMaterial().get(player) != null)
            createItemLore.set(6, Utils.colorize("&7Material: " + String.valueOf(MultiCurrency.getInventoryCache().getMaterial().get(player)).toUpperCase()));

        ItemMeta createItemMeta1 = createItem.getItemStack().getItemMeta();
        createItemMeta1.setLore(createItemLore);
        createItem.getItemStack().setItemMeta(createItemMeta1);

        gui.updateItem(22, createItem);
    }
}
