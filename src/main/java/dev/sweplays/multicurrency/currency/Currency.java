package dev.sweplays.multicurrency.currency;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.UUID;

public class Currency {

    @Getter
    @Setter
    private UUID uuid;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private double startingBalance;

    @Getter
    @Setter
    private Material inventoryMaterial;

    public Currency(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
