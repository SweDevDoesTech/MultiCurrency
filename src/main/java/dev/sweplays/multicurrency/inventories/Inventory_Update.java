package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Inventory_Update {

    final AnvilGUI.Builder anvilGui;

    public Inventory_Update(Currency currency, InventoryType type) {
        anvilGui = new AnvilGUI.Builder();
        anvilGui.plugin(MultiCurrency.getInstance());

        if (type == InventoryType.UPDATE_SINGULAR)
            anvilGui.title("Enter Singular Name");

        else if (type == InventoryType.UPDATE_PLURAL)
            anvilGui.title("Enter Plural Name");

        else if (type == InventoryType.UPDATE_SYMBOL)
            anvilGui.title("Enter Symbol");

        else if (type == InventoryType.UPDATE_DEFAULT_BALANCE)
            anvilGui.title("Enter Default Balance");

        List<String> itemLeftLore = new ArrayList<>();
        itemLeftLore.add("");

        itemLeftLore.add(Utils.colorize("&7Enter name above."));
        itemLeftLore.add("");

        ItemStack itemLeft = new ItemStack(Material.PAPER);
        ItemMeta itemLeftMeta = itemLeft.getItemMeta();
        if (type == InventoryType.SET_SINGULAR)
            itemLeftLore.add(Utils.colorize("&7Enter singular above."));

        else if (type == InventoryType.SET_PLURAL)
            itemLeftLore.add(Utils.colorize("&7Enter plural above."));

        else if (type == InventoryType.SET_SYMBOL)
            itemLeftLore.add(Utils.colorize("&7Enter symbol above."));

        else if (type == InventoryType.SET_DEFAULT_BALANCE)
            itemLeftLore.add(Utils.colorize("&7Enter default balance above."));

        assert itemLeftMeta != null;
        itemLeftMeta.setLore(itemLeftLore);
        itemLeft.setItemMeta(itemLeftMeta);

        anvilGui.itemLeft(itemLeft);

        anvilGui.onComplete((player, text) -> {
            Inventory_UpdateCurrency inventory_updateCurrency = new Inventory_UpdateCurrency(currency);

            for (Currency currency1 : MultiCurrency.getCurrencyManager().getCurrencies()) {
                if (currency1.getSingular().equals(text)) {
                    player.sendMessage(Utils.colorize("&cA currency with this name already exists."));
                    return AnvilGUI.Response.openInventory(anvilGui.open(player).getInventory());
                }
            }

            if (type == InventoryType.UPDATE_SINGULAR) {
                currency.setSingular(text);
                new Inventory_Update(currency, InventoryType.UPDATE_PLURAL).openInventory(player);
                return AnvilGUI.Response.close();

            } else if (type == InventoryType.UPDATE_PLURAL) {
                currency.setPlural(text);

            } else if (type == InventoryType.UPDATE_DEFAULT_BALANCE) {
                if (text.matches("[0-9]+"))
                    currency.setDefaultBalance(Double.parseDouble(text));
                else {
                    player.sendMessage(Utils.colorize("&cThe text must only container numbers."));
                    anvilGui.open(player);
                    return AnvilGUI.Response.close();
                }
            } else if (type == InventoryType.UPDATE_SYMBOL) {
                currency.setSymbol(text);
            }
            inventory_updateCurrency.openInventory(player);
            return AnvilGUI.Response.close();
        });
    }

    public void openInventory(Player player) {
        anvilGui.open(player);
    }
}
