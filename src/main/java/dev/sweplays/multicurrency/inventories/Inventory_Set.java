package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Inventory_Set {

    final AnvilGUI.Builder anvilGui;

    public Inventory_Set(InventoryType type) {
        anvilGui = new AnvilGUI.Builder();
        anvilGui.plugin(MultiCurrency.getInstance());

        if (type == InventoryType.SET_SINGULAR)
            anvilGui.title("Enter Singular Name");

        else if (type == InventoryType.SET_PLURAL)
            anvilGui.title("Enter Plural Name");

        else if (type == InventoryType.SET_SYMBOL)
            anvilGui.title("Enter Symbol");

        else if (type == InventoryType.SET_DEFAULT_BALANCE)
            anvilGui.title("Enter Default Balance");

        List<String> itemLeftLore = new ArrayList<>();
        itemLeftLore.add("");
        if (type == InventoryType.SET_SINGULAR)
            itemLeftLore.add(Utils.colorize("&7Enter singular above."));

        else if (type == InventoryType.SET_PLURAL)
            itemLeftLore.add(Utils.colorize("&7Enter plural above."));

        else if (type == InventoryType.SET_SYMBOL)
            itemLeftLore.add(Utils.colorize("&7Enter symbol above."));

        else if (type == InventoryType.SET_DEFAULT_BALANCE)
            itemLeftLore.add(Utils.colorize("&7Enter default balance above."));

        itemLeftLore.add("");

        ItemStack itemLeft = new ItemStack(Material.PAPER);
        ItemMeta itemLeftMeta = itemLeft.getItemMeta();
        itemLeftMeta.setDisplayName(Utils.colorize("&7Enter above"));
        itemLeftMeta.setLore(itemLeftLore);
        itemLeft.setItemMeta(itemLeftMeta);

        anvilGui.itemLeft(itemLeft);

        anvilGui.onComplete((player, text) -> {
            Inventory_CreateCurrency inventory_createCurrency = new Inventory_CreateCurrency(player);

            if (type == InventoryType.SET_SINGULAR) {
                MultiCurrency.getInventoryCache().getCurrencySingular().put(player, text);
                new Inventory_Set(InventoryType.SET_PLURAL).openInventory(player);
                return AnvilGUI.Response.close();

            } else if (type == InventoryType.SET_PLURAL) {
                MultiCurrency.getInventoryCache().getCurrencyPlural().put(player, text);

            } else if (type == InventoryType.SET_DEFAULT_BALANCE) {
                if (text.matches("[0-9]+"))
                    MultiCurrency.getInventoryCache().getDefaultBalance().put(player, Double.valueOf(text));
                else {
                    player.sendMessage(Utils.colorize("&cThe text must only container numbers."));
                    anvilGui.open(player);
                    return AnvilGUI.Response.close();
                }
            } else if (type == InventoryType.SET_SYMBOL) {
                MultiCurrency.getInventoryCache().getSymbol().put(player, text);
            }
            inventory_createCurrency.openInventory(player);
            inventory_createCurrency.updateCreationLore();
            return AnvilGUI.Response.close();
        });
    }

    public void openInventory(Player player) {
        anvilGui.open(player);
    }
}
