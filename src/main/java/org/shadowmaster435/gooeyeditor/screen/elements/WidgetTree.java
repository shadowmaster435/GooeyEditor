package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.client.gui.Drawable;

public class WidgetTree extends GuiElement{

    public WidgetTree(Drawable widget, int x, int y, boolean editMode) {
        super(widget, x, y, editMode);
    }

    @Override
    public Property[] getProperties() {
        return new Property[0];
    }
}
