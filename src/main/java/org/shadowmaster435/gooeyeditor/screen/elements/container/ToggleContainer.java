package org.shadowmaster435.gooeyeditor.screen.elements.container;

import org.joml.Vector2i;

public class ToggleContainer extends CollapsableContainer {
    public ToggleContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }

    @Override
    public Vector2i getClosedSize() {
        return new Vector2i(0,0);
    }

    @Override
    public Vector2i getOpenSize() {
        return getStoredSize();
    }

    @Override
    public void arrange() {

    }
}
