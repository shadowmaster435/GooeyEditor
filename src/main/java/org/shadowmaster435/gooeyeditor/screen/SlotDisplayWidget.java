package org.shadowmaster435.gooeyeditor.screen;

import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

public class SlotDisplayWidget extends GuiElement {
    public SlotDisplayWidget(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}
