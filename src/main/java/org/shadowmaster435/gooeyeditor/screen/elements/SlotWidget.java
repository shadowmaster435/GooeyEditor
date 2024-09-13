package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.ColorHelper;

public class SlotWidget extends ParentableWidgetBase {

    public Slot displayedSlot;
    public boolean drawSlot = true;

    public SlotWidget(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }
    public SlotWidget(int x, int y, int w, int h, Slot slot, boolean editMode) {
        super(x, y, w, h, editMode);
        this.displayedSlot = slot;
    }

    public SlotWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }


    public void setDisplayedSlot(Slot slot) {
        this.displayedSlot = slot;
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {

        if (drawSlot) {
            drawNinePatchTexture(context, getGlobalRect(), NinePatchTexture.SLOT.texture(),  2,  NinePatchTexture.SLOT.texture_width(),  NinePatchTexture.SLOT.texture_height());
        }
        if (displayedSlot == null) {
            if (isMouseOver(mouseX, mouseY) && !isEditMode()) {

                context.fill(getGlobalX() + 1, getGlobalY() + 1, getGlobalX() + (getWidth() - 1), getGlobalY() + (getHeight() - 1), (int) ((layer + 1) + (getSize().length())), ColorHelper.Argb.getArgb(20,255, 255, 255));

            }
            super.preTransform(context, mouseX, mouseY, delta);

            return;
        }

        try {
            drawItem(context, displayedSlot.getStack(), getGlobalX() + 1, getGlobalY() + 1, getWidth() - 2, getHeight() - 2);
            if (isMouseOver(mouseX, mouseY) && !isEditMode()) {
                context.fill(getGlobalX() + 1, getGlobalY() + 1, getGlobalX() + (getWidth() - 1), getGlobalY() + (getHeight() - 1), (int) ((layer + 1) + (getSize().length())), ColorHelper.Argb.getArgb(20,255, 255, 255));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Property[] getProperties() {
        var drawSlot = new Property("Draw Slot Texture", "drawSlot", "drawSlot", Boolean.class);
        return new Property[]{drawSlot};
    }
}
