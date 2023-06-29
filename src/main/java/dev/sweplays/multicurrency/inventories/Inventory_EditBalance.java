package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.currency.Currency;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.Messages;
import dev.sweplays.multicurrency.utilities.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Inventory_EditBalance {

    final AnvilGUI.Builder anvilGui;

    public Inventory_EditBalance(InventoryType type, Account account, Currency currency) {
        anvilGui = new AnvilGUI.Builder();
        anvilGui.plugin(MultiCurrency.getInstance());

        anvilGui.title("Enter Value");

        List<String> itemLeftLore = new ArrayList<>();
        itemLeftLore.add("");
        itemLeftLore.add(Utils.colorize("&7Enter value above."));
        itemLeftLore.add("");

        ItemStack itemLeft = new ItemStack(Material.PAPER);
        ItemMeta itemLeftMeta = itemLeft.getItemMeta();
        itemLeftMeta.setDisplayName(Utils.colorize("&7Enter value"));
        itemLeftMeta.setLore(itemLeftLore);
        itemLeft.setItemMeta(itemLeftMeta);

        anvilGui.itemLeft(itemLeft);

        anvilGui.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return List.of(AnvilGUI.ResponseAction.close());
            }

            String text = stateSnapshot.getText();
            Player player = stateSnapshot.getPlayer();
            Player target = Bukkit.getPlayer(account.getOwnerUuid());

            if (type == InventoryType.SET_BALANCE) {
                if (text.matches("[0-9]+")) {
                    account.updateBalance(currency, Double.parseDouble(text), true);
                    target.sendMessage(Utils.colorize(Messages.SET_SUCCESS_TARGET.get(Double.parseDouble(text))
                            .replace("{player}", player.getName())
                            .replace("{currency}", currency.getSingular())
                            .replace("{amount}", String.valueOf(Double.parseDouble(text)))
                            .replace("{symbol}", currency.getSymbol())
                    ));
                } else {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    anvilGui.open(player);
                    return List.of(AnvilGUI.ResponseAction.close());
                }
            } else if (type == InventoryType.ADD_BALANCE) {
                if (text.matches("[0-9]+")) {
                    double finalAmount = account.getBalance(currency) + Double.parseDouble(text);
                    account.updateBalance(currency, finalAmount, true);
                    target.sendMessage(Utils.colorize(Messages.ADD_SUCCESS_TARGET.get(Double.parseDouble(text))
                            .replace("{player}", player.getName())
                            .replace("{currency}", currency.getSingular())
                            .replace("{amount}", String.valueOf(Double.parseDouble(text)))
                            .replace("{symbol}", currency.getSymbol())
                    ));
                } else {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    anvilGui.open(player);
                    return List.of(AnvilGUI.ResponseAction.close());
                }
            } else if (type == InventoryType.REMOVE_BALANCE) {
                if (text.matches("[0-9]+")) {
                    if (account.getBalance(currency) < Double.parseDouble(text)) {
                        player.sendMessage(Utils.colorize(Messages.UNDER_ZERO.get()
                                .replace("{prefix}", Messages.PREFIX.get())
                        ));
                        anvilGui.open(player);
                        return List.of(AnvilGUI.ResponseAction.close());
                    }
                    double finalAmount = account.getBalance(currency) - Double.parseDouble(text);
                    account.updateBalance(currency, finalAmount, true);
                    target.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS_TARGET.get(Double.parseDouble(text))
                            .replace("{player}", player.getName())
                            .replace("{currency}", currency.getSingular())
                            .replace("{amount}", String.valueOf(Double.parseDouble(text)))
                            .replace("{symbol}", currency.getSymbol())
                    ));
                } else {
                    player.sendMessage(Utils.colorize(Messages.ONLY_NUMBERS.get()
                            .replace("{prefix}", Messages.PREFIX.get())
                    ));
                    anvilGui.open(player);
                    return List.of(AnvilGUI.ResponseAction.close());
                }
            }
            Inventory_UpdatePlayer inventory_updatePlayer = new Inventory_UpdatePlayer(account);
            inventory_updatePlayer.openInventory(player);

            return List.of(AnvilGUI.ResponseAction.close());
        });
    }

    public void openInventory(Player player) {
        anvilGui.open(player);
    }
}
