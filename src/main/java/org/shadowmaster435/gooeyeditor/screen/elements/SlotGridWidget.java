package org.shadowmaster435.gooeyeditor.screen.elements;

import com.google.common.collect.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;
import org.shadowmaster435.gooeyeditor.screen.editor.util.MiscMath;

import java.util.List;

public class SlotGridWidget extends GuiElement implements MiscMath {

    private final Table<Integer, Integer, SlotWidget> slotTable = HashBasedTable.create();
    public int xSize = 2;
    public int ySize = 2;
    public int xSpacing = 0;
    public int ySpacing = 0;
    public int slotWidth = 16;
    public int slotHeight = 16;
    public boolean regen = false;

    public SlotGridWidget(int x, int y, int xSize, int ySize, int slotWidth, int slotHeight, int xSpacing, int ySpacing, boolean editMode) {
        super(x, y, editMode);
        this.xSize = xSize;
        this.ySize = ySize;
        this.xSpacing = xSpacing;
        this.ySpacing = ySpacing;
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
        this.setWidth(32);
        this.setHeight(32);

        createGrid();
        showChildren = false;

    }




    public SlotGridWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        this.setWidth(32);
        this.setHeight(32);
        createGrid();
        showChildren = false;

    }

    public void createGrid() {
        slotTable.clear();
        clearChildren();
        for (int x = 0; x < Math.abs(xSize); ++x) {
            for (int y = 0; y < Math.abs(ySize); ++y) {
                var widget = new SlotWidget(32,32, slotWidth, slotHeight, false);
                widget.drawSlot = true;
                widget.setWidth(slotWidth);
                widget.setHeight(slotHeight);
                widget.setX(x * (slotWidth + xSpacing));
                widget.setY(y * (slotHeight + ySpacing));
                widget.selectable = false;
                widget.needsExport = false;
                widget.layer = this.layer;
                widget.name = String.valueOf(x + (y * ySize));
                addElement(widget);
                slotTable.put(x, y, widget);
            }
        }
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (regen) {
            clearChildren();
            createGrid();
            regen = false;
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    /**
     * @return {@link SlotWidget} by their position in the grid or null if not present.
     */
    public SlotWidget getSlot(int x, int y) {
        return slotTable.get(x, y);
    }
    /**
     * @return {@link SlotWidget} by their numerical id from left to right, top to bottom (see {@link #gridify(int, int)}), or null if not present.
     */
    public SlotWidget getSlot(int i) {
        var pos = gridify(i, xSize);


        return getSlot(pos.x, pos.y);
    }

    /**
     * Assigns given slots by their numerical id from left to right, top to bottom (see {@link #gridify(int, int)}).
     */
    public void setSlots(Slot... slots) {
        for (int i = 0; i < slots.length; ++i) {
            getSlot(i).displayedSlot = slots[i];
        }
    }
    /**
     * Assigns given slots by their numerical id from left to right, top to bottom (see {@link #gridify(int, int)}).
     */
    public void setSlots(List<Slot> slots) {
        for (int i = 0; i < slots.size(); ++i) {
            getSlot(i).displayedSlot = slots.get(i);
        }
    }



    @Override
    public Property[] getProperties() {
        var xSize = new Property("Grid Width", "xSize", "xSize", Integer.class);
        var ySize = new Property("Grid Height", "ySize", "ySize", Integer.class);
        var slotWidth = new Property("Slot Width", "slotWidth", "slotWidth", Integer.class);
        var slotHeight = new Property("Slot Height", "slotHeight", "slotHeight", Integer.class);
        var xSpacing = new Property("X Spacing", "xSpacing", "xSpacing", Integer.class);
        var ySpacing = new Property("Y Spacing", "ySpacing", "ySpacing", Integer.class);
        var regen = new Property("Regenerate Grid", "regen", "regen", Boolean.class);

        return new Property[]{regen, xSize, ySize, slotWidth, slotHeight, xSpacing, ySpacing};
    }
}
