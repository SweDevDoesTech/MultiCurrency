package dev.sweplays.multicurrency.inventories;

import dev.sweplays.multicurrency.utilities.InventoryType;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

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
