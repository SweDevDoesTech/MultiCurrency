package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.Messages;
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

        List<String> itemLeftLore = new ArrayList<>();
        itemLeftLore.add("");
        itemLeftLore.add("");

        ItemStack itemLeft = new ItemStack(Material.PAPER);
        ItemMeta itemLeftMeta = itemLeft.getItemMeta();
        assert itemLeftMeta != null;
        if (type == InventoryType.UPDATE_SINGULAR) {
            anvilGui.title("Enter Singular Name");
            itemLeftLore.add(Utils.colorize("&7Enter singular above."));
        }
        else if (type == InventoryType.UPDATE_PLURAL) {
            anvilGui.title("Enter Plural Name");
            itemLeftLore.add(Utils.colorize("&7Enter plural above."));
        }
        else if (type == InventoryType.UPDATE_SYMBOL) {
            anvilGui.title("Enter Symbol");
            itemLeftLore.add(Utils.colorize("&7Enter symbol above."));
        }
        else if (type == InventoryType.UPDATE_DEFAULT_BALANCE) {
            anvilGui.title("Enter Default Balance");
            itemLeftLore.add(Utils.colorize("&7Enter default balance above."));
        }

        itemLeftMeta.setLore(itemLeftLore);
        itemLeftMeta.setDisplayName(Utils.colorize("&7Enter Above."));
        itemLeft.setItemMeta(itemLeftMeta);

        anvilGui.itemLeft(itemLeft);

        anvilGui.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return List.of(AnvilGUI.ResponseAction.close());
            }

            Player player = stateSnapshot.getPlayer();
            String text = stateSnapshot.getText();

            for (Currency currency1 : MultiCurrency.getCurrencyManager().getCurrencies()) {
                if (currency1.getSingular().equals(text)) {
                    player.sendMessage(Utils.colorize(Messages.CREATE_ERROR.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    return List.of(AnvilGUI.ResponseAction.openInventory(anvilGui.open(player).getInventory()));
                }
            }

            if (type == InventoryType.UPDATE_SINGULAR) {
                currency.setSingular(text);
                new Inventory_Update(currency, InventoryType.UPDATE_PLURAL).openInventory(player);
                return List.of(AnvilGUI.ResponseAction.close());

            } else if (type == InventoryType.UPDATE_PLURAL) {
                currency.setPlural(text);

            } else if (type == InventoryType.UPDATE_DEFAULT_BALANCE) {
                if (text.matches("[0-9]+"))
                    currency.setDefaultBalance(Double.parseDouble(text));
                else {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    anvilGui.open(player);
                    return List.of(AnvilGUI.ResponseAction.close());
                }
            } else if (type == InventoryType.UPDATE_SYMBOL) {
                currency.setSymbol(text);
            }
            Inventory_UpdateCurrency inventory_updateCurrency = new Inventory_UpdateCurrency(currency);
            inventory_updateCurrency.openInventory(player);

            return List.of(AnvilGUI.ResponseAction.close());
        });
    }

    public void openInventory(Player player) {
        anvilGui.open(player);
    }
}
