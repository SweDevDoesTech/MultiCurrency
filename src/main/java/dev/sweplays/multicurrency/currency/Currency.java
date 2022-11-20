package dev.sweplays.multicurrency.currency;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.UUID;

public class Currency {

    @Getter
    private final UUID uuid;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private double defaultBalance;

    @Getter
    @Setter
    private boolean payable;

    @Getter
    @Setter
    private boolean isDefault;

    @Getter
    @Setter
    private Material inventoryMaterial;

    @Getter
    @Setter
    private ChatColor color;

    public Currency(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Currency(UUID uuid, String name, String symbol, Double defaultBalance, boolean payable, Material inventoryMaterial) {
        this.uuid = uuid;
        this.name = name;
        this.symbol = symbol;
        this.defaultBalance = defaultBalance;
        this.payable = payable;
        this.inventoryMaterial = inventoryMaterial;
    }
}
