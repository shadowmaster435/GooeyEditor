package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.screen.slot.Slot;

public class SlotWidget extends ParentableWidgetBase {

    public Slot displayedSlot;
    public boolean drawSlot = true;

    public SlotWidget(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }
    public SlotWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }
    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        if (displayedSlot == null) {

            super.preTransform(context, mouseX, mouseY, delta);

            return;
        }
        if (drawSlot) {
            drawNinePatchTexture(context, getRect(), NinePatchTexture.GUI_SLOT.texture(),  NinePatchTexture.GUI_SLOT.edge_thickness(),  NinePatchTexture.GUI_SLOT.texture_width(),  NinePatchTexture.GUI_SLOT.texture_height());
        }
        try {
            drawItem(context, displayedSlot.getStack(), getX(), getY(), getWidth(), getHeight());
        } catch (Exception ignored) {}
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            if (button == 0) {
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Property[] getProperties() {
        var drawSlot = new Property("Draw Slot Texture", "drawSlot", "drawSlot", Boolean.class);
        return new Property[]{drawSlot};
    }
}
