package org.shadowmaster435.gooeyeditor.screen.elements;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import org.shadowmaster435.gooeyeditor.screen.GuiScreenHandler;
import org.shadowmaster435.gooeyeditor.screen.HandledGuiScreen;

import java.util.ArrayList;

public class PlayerInventoryWidget extends ParentableWidgetBase {

    private SlotGridWidget inventory;
    private SlotGridWidget hotbar;

    public PlayerInventoryWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        SlotGridWidget inventory = new SlotGridWidget(0,0,9, 3, 16,16,0,0,true);
        SlotGridWidget hotbar = new SlotGridWidget(0,0,9, 1, 16,16,0,0,true);

        this.inventory = inventory;
        this.hotbar = hotbar;
        addElements(inventory, hotbar);
    }

    public void setPlayerInventory(HandledGuiScreen<? extends GuiScreenHandler> handled) {
        ArrayList<Slot> hotbarSlots = new ArrayList<>();
        ArrayList<Slot> inventorySlots = new ArrayList<>();
        for (int i = 0; i < 36; ++i) {
            if (i < 9) {
                hotbarSlots.add(handled.getScreenHandler().getSlot(i));
            } else {
                inventorySlots.add(handled.getScreenHandler().getSlot(i));
            }
        }
        this.inventory.setSlots(inventorySlots.toArray(new Slot[]{}));
        this.hotbar.setSlots(hotbarSlots.toArray(new Slot[]{}));
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        super.preTransform(context, mouseX, mouseY, delta);
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
