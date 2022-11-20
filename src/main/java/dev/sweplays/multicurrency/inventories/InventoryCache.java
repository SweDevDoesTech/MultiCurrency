package dev.sweplays.multicurrency.inventories;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InventoryCache {

    @Getter
    private final Map<Player, String> currencyName;

    @Getter
    private final Map<Player, String> symbol;

    @Getter
    private final Map<Player, Double> defaultBalance;

    @Getter
    private final Map<Player, Boolean> payable;

    @Getter
    private final Map<Player, Boolean> isDefault;

    @Getter
    private final Map<Player, Material> material;

    public InventoryCache() {
        currencyName = new HashMap<>();
        symbol = new HashMap<>();
        defaultBalance = new HashMap<>();
        payable = new HashMap<>();
        isDefault = new HashMap<>();
        material = new HashMap<>();
    }

    public void clearCache(Player player) {
        getCurrencyName().remove(player);
        getSymbol().remove(player);
        getDefaultBalance().remove(player);
        getPayable().remove(player);
        getIsDefault().remove(player);
        getMaterial().remove(player);
    }
}
