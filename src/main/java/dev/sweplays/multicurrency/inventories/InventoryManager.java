package dev.sweplays.multicurrency.inventories;

import lombok.Getter;

public class InventoryManager {

    @Getter
    private final Inventory_Main mainInventory;

    //@Getter
    //private final Inventory_Options optionsInventory;

    //@Getter
    //private final Inventory_CurrencyList currencyListInventory;

    public InventoryManager() {
        mainInventory = new Inventory_Main();
        //optionsInventory = new Inventory_Options(InventoryType.SET);
        //currencyListInventory = new Inventory_CurrencyList();
    }
}
