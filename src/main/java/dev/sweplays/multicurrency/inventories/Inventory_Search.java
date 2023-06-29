package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
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

public class Inventory_Search {

    final AnvilGUI.Builder anvilGui;

    public Inventory_Search() {
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

        anvilGui.onClick((slot, stateSnapshot) -> {
            if (slot != AnvilGUI.Slot.OUTPUT) {
                return List.of(AnvilGUI.ResponseAction.close());
            }

            String text = stateSnapshot.getText();
            Player player = stateSnapshot.getPlayer();
            Player target = Bukkit.getPlayer(text);

            if (target == null) {
                player.sendMessage(Utils.colorize(Messages.PLAYER_NOT_FOUND.get()
                        .replace("{target}", text)
                ));
                anvilGui.open(player);
                return List.of(AnvilGUI.ResponseAction.close());
            }

            Account account = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            if (account != null) {
                new Inventory_UpdatePlayer(account).openInventory(player);
            }
            return List.of(AnvilGUI.ResponseAction.close());
        });
    }

    public void openInventory(Player player) {
        anvilGui.open(player);
    }
}
