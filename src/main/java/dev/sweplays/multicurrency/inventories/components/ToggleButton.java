package dev.sweplays.multicurrency.inventories.components;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.Getter;
public class ToggleButton {

    @Getter
    private boolean enabled = false;

    @Getter
    private int slot;

    public ToggleButton(int slot, boolean enabled) {
        this.enabled = enabled;
        this.slot = slot;
    }

    public void setEnabledItem(GuiItem item, Gui gui) {
        gui.setItem(slot, item);
    }

    public void setDisabledItem(GuiItem item, Gui gui) {
        gui.setItem(slot, item);
    }

    public void toggle() {
        enabled = !enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
