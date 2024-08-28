package org.shadowmaster435.gooeyeditor.screen.elements;

import com.google.common.collect.*;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import org.objectweb.asm.Handle;
import org.shadowmaster435.gooeyeditor.screen.GuiScreenHandler;
import org.shadowmaster435.gooeyeditor.screen.HandledGuiScreen;
import org.shadowmaster435.gooeyeditor.screen.SlotDisplayWidget;

import java.util.ArrayList;

public class SlotGridWidget extends ParentableWidgetBase {

    private final Table<Integer, Integer, SlotWidget> slotTable = HashBasedTable.create();
    public int xSize = 2;
    public int ySize = 2;
    public int xSpacing = 0;
    public int ySpacing = 0;
    public int slotWidth = 16;
    public int slotHeight = 16;


    public SlotGridWidget(int x, int y, int xSize, int ySize, int slotWidth, int slotHeight, int xSpacing, int ySpacing, boolean editMode) {
        super(x, y, editMode);
        this.xSize= xSize;
        this.ySize = ySize;
        this.xSpacing = xSpacing;
        this.ySpacing = ySpacing;
        this.slotWidth = slotWidth;
        this.slotHeight = slotHeight;
        createGrid();
    }

    public SlotGridWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
        createGrid();
    }

    protected void createGrid() {
        slotTable.clear();
        for (int x = 0; x < Math.abs(xSize); ++x) {
            for (int y = 0; y < Math.abs(ySize); ++y) {
                var widget = new SlotWidget(0,0, slotWidth, slotHeight, this.isEditMode());
                widget.layer = this.layer;
                addElement(widget);
                slotTable.put(x, y, widget);
            }
        }
    }

    public ArrayList<Slot> getAndAddSlotsToHandler(HandledGuiScreen<? extends GuiScreenHandler> handled, Inventory inventory) {
        ArrayList<Slot> ret = new ArrayList<>();
        for (int x = 0; x < Math.abs(xSize); ++x) {
            for (int y = 0; y < Math.abs(ySize); ++y) {
                var widget = slotTable.get(x, y);
                if (widget == null) {
                    continue;
                }
                var slot = new Slot(inventory, x + (xSize * y), -1000, -1000);
                widget.displayedSlot = slot;
                handled.getScreenHandler().addSlot(slot);
            }
        }
        return ret;
    }


    @Override
    public Property[] getProperties() {
        var xSize = new Property("Grid Width", "xSize", "xSize", Integer.class);
        var ySize = new Property("Grid Height", "ySize", "ySize", Integer.class);
        var slotWidth = new Property("Slot Width", "slotWidth", "slotWidth", Integer.class);
        var slotHeight = new Property("Slot Height", "slotHeight", "slotHeight", Integer.class);
        var xSpacing = new Property("X Spacing", "xSpacing", "xSpacing", Integer.class);
        var ySpacing = new Property("Y Spacing", "ySpacing", "ySpacing", Integer.class);
        return new Property[]{xSize, ySize, slotWidth, slotHeight, xSpacing, ySpacing};
    }
}
