package org.shadowmaster435.gooeyeditor.screen.elements;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.entity.player.PlayerInventory;
import org.shadowmaster435.gooeyeditor.screen.GuiScreenHandler;
import org.shadowmaster435.gooeyeditor.screen.HandledGuiScreen;

public class PlayerInventoryWidget extends SlotGridWidget{

    public PlayerInventoryWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        xSize = 9;
        ySize = 4;
        createGrid();
    }

    public void setPlayerInventory(HandledGuiScreen<? extends GuiScreenHandler> handled, PlayerInventory inventory) {
        getAndAddSlotsToHandler(handled, inventory);
    }



    @Override
    public Property[] getProperties() {
        var slotWidth = new Property("Slot Width", "slotWidth", "slotWidth", Integer.class);
        var slotHeight = new Property("Slot Height", "slotHeight", "slotHeight", Integer.class);
        var xSpacing = new Property("X Spacing", "xSpacing", "xSpacing", Integer.class);
        var ySpacing = new Property("Y Spacing", "ySpacing", "ySpacing", Integer.class);
        return new Property[]{slotWidth, slotHeight, xSpacing, ySpacing};
    }
}
