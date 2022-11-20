package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.MultiCurrency;
import dev.sweplays.multicurrency.account.Account;
import dev.sweplays.multicurrency.utilities.InventoryType;
import dev.sweplays.multicurrency.utilities.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Inventory_Search {

    AnvilGUI.Builder anvilGui;

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

        anvilGui.onComplete((player, text) -> {
            Player target = Bukkit.getPlayer(text);
            if (target == null) {
                player.sendMessage(Utils.colorize("&cCould not find a player with that name."));
                anvilGui.open(player);
                return AnvilGUI.Response.close();
            }

            Account account = MultiCurrency.getAccountManager().getAccount(target.getUniqueId());

            if (account != null) {
                new Inventory_UpdatePlayer(account).openInventory(player);
            }
            return AnvilGUI.Response.close();
        });
    }

    public void openInventory(Player player) {
        anvilGui.open(player);
    }
}
