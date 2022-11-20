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

    AnvilGUI.Builder anvilGui;

    public Inventory_Update(Currency currency, InventoryType type) {
        anvilGui = new AnvilGUI.Builder();
        anvilGui.plugin(MultiCurrency.getInstance());

        anvilGui.title("Enter Name");

        List<String> itemLeftLore = new ArrayList<>();
        itemLeftLore.add("");
        itemLeftLore.add(Utils.colorize("&7Enter name above."));
        itemLeftLore.add("");

        ItemStack itemLeft = new ItemStack(Material.PAPER);
        ItemMeta itemLeftMeta = itemLeft.getItemMeta();
        itemLeftMeta.setDisplayName(Utils.colorize("&7Enter name"));
        itemLeftMeta.setLore(itemLeftLore);
        itemLeft.setItemMeta(itemLeftMeta);

        anvilGui.itemLeft(itemLeft);

        anvilGui.onComplete((player, text) -> {
            Inventory_UpdateCurrency inventory_updateCurrency = new Inventory_UpdateCurrency(currency);

            for (Currency currency1 : MultiCurrency.getCurrencyManager().getCurrencies()) {
                if (currency1.getName().equals(text)) {
                    player.sendMessage(Utils.colorize("&cA currency with this name already exists."));
                    return AnvilGUI.Response.openInventory(anvilGui.open(player).getInventory());
                }
            }

            if (type == InventoryType.UPDATE_NAME) {
                currency.setName(text);
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
