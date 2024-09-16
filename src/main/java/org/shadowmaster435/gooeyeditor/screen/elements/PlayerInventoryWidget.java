package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;

public class PlayerInventoryWidget extends ParentableWidgetBase {

    private SlotGridWidget inventory;
    private SlotGridWidget hotbar;
    public int slotWidth = 18;
    public int slotHeight = 18;
    public PlayerInventoryWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        SlotGridWidget inventory = new SlotGridWidget(0,0,9, 3, slotWidth,slotHeight,0,0,editMode);
        SlotGridWidget hotbar = new SlotGridWidget(0,(slotHeight * 3) + 2,9, 1, slotWidth,slotHeight,0,0,editMode);
        hotbar.selectable = false;
        inventory.selectable = false;



        hotbar.showChildren = false;
        inventory.showChildren = false;
        setSize(slotWidth * 9, (slotHeight * 4) + 2);
        addElement(hotbar);
        addElement(inventory);
        this.inventory = inventory;
        this.hotbar = hotbar;
        this.hotbar.needsExport = false;
        this.inventory.needsExport = false;
    }

    public SlotWidget getHoveredSlot(int mouseX, int mouseY) {
        SlotWidget result = null;
        GuiElement inv = inventory.getHoveredChild(mouseX, mouseY);
        GuiElement hotbar = this.hotbar.getHoveredChild(mouseX, mouseY);

        if (hotbar instanceof SlotWidget widget) {
            return widget;
        }
        if (inv instanceof SlotWidget widget) {
            return widget;
        }
        return result;
    }

    public void setHotbar(ArrayList<Slot> slots) {
        hotbar.setSlots(slots);
    }
    public void setPlayerInventory(ArrayList<Slot> slots) {
        inventory.setSlots(slots);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        this.hotbar.needsExport = false;
        this.inventory.needsExport = false;
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public Property[] getProperties() {
        var slotWidth = new Property("Slot Width", "slotWidth", "slotWidth", Integer.class);
        var slotHeight = new Property("Slot Height", "slotHeight", "slotHeight", Integer.class);
        return new Property[]{slotWidth, slotHeight};
    }
}
