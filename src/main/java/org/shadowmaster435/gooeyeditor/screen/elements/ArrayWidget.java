package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.DrawContext;
import org.shadowmaster435.gooeyeditor.util.InputHelper;

import java.util.Arrays;

public class ArrayWidget<W extends GuiElement> extends ParentableWidgetBase {


    private int entry_offset = 0;

    public ArrayWidget(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }

    public ArrayWidget(int x, int y, boolean editMode, W... preset) {
        super(x, y, editMode);
        widgets.addAll(Arrays.stream(preset).toList());
    }

    public int getEntryOffset() {
        return entry_offset;
    }

    public void setEntryOffset(int entry_offset) {
        this.entry_offset = entry_offset;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (InputHelper.isCtrlHeld && InputHelper.isShiftHeld && button == 0) {
            removeChild(getHoveredChild((int) mouseX, (int) mouseY));
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        super.preTransform(context, mouseX, mouseY, delta);
    }
}
