package org.shadowmaster435.gooeyeditor.screen.elements.container;

import org.joml.Vector2i;

/**
 * Functions identically to {@link CollapsableContainer} but can be instantiated.
 */
public class ToggleContainer extends CollapsableContainer {
    public ToggleContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }
    public ToggleContainer(int x, int y, boolean editMode) {
        super(x, y, 32, 32, editMode);
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
