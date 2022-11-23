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
            Inventory_UpdatePlayer inventory_updatePlayer = new Inventory_UpdatePlayer(account);

            Player target = Bukkit.getPlayer(account.getOwnerUuid());

            if (type == InventoryType.SET_BALANCE) {
                if (text.matches("[0-9]+")) {
                    account.updateBalance(currency, Double.parseDouble(text), true);
                    target.sendMessage(Utils.colorize(Messages.SET_SUCCESS_TARGET.get()
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
                    return AnvilGUI.Response.close();
                }
            } else if (type == InventoryType.ADD_BALANCE) {
                if (text.matches("[0-9]+")) {
                    double finalAmount = account.getBalance(currency) + Double.parseDouble(text);
                    account.updateBalance(currency, finalAmount, true);
                    target.sendMessage(Utils.colorize(Messages.ADD_SUCCESS_TARGET.get()
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
                    return AnvilGUI.Response.close();
                }
            } else if (type == InventoryType.REMOVE_BALANCE) {
                if (text.matches("[0-9]+")) {
                    if (account.getBalance(currency) <= Double.parseDouble(text)) {
                        player.sendMessage(Utils.colorize(Messages.UNDER_ZERO.get()
                                .replace("{prefix}", Messages.PREFIX.get())
                        ));
                        anvilGui.open(player);
                        return AnvilGUI.Response.close();
                    }
                    double finalAmount = account.getBalance(currency) - Double.parseDouble(text);
                    account.updateBalance(currency, finalAmount, true);
                    target.sendMessage(Utils.colorize(Messages.REMOVE_SUCCESS_TARGET.get()
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
                    return AnvilGUI.Response.close();
                }
            }
            inventory_updatePlayer.openInventory(player);
            return AnvilGUI.Response.close();
        });
    }

    public void openInventory(Player player) {
        anvilGui.open(player);
    }
}
