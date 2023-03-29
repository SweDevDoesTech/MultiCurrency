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
    private String singular;

    @Getter
    @Setter
    private String plural;

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

    @Getter
    @Setter
    private String id;

    public Currency(UUID uuid, String singular, String plural) {
        this.uuid = uuid;
        this.singular = singular;
        this.plural = plural;
    }

    public Currency(UUID uuid, String singular, String plural, String symbol, Double defaultBalance, boolean payable, Material inventoryMaterial) {
        this.uuid = uuid;
        this.singular = singular;
        this.plural = plural;
        this.symbol = symbol;
        this.defaultBalance = defaultBalance;
        this.payable = payable;
        this.inventoryMaterial = inventoryMaterial;
    }
}
