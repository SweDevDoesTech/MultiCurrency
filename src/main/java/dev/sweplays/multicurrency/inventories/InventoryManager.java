package dev.sweplays.multicurrency.inventories;

import lombok.Getter;

public class InventoryManager {

    @Getter
    private final Inventory_Main mainInventory;

    public InventoryManager() {
        mainInventory = new Inventory_Main();
    }
}
