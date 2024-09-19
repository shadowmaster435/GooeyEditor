package org.shadowmaster435.gooeyeditor.screen.elements.container;


/**
 * Generic container useful for grouping or clipping elements.
 */
public class GenericContainer extends BaseContainer {
    public GenericContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }
    public GenericContainer(int x, int y, boolean editMode) {
        super(x, y,32, 32, editMode);
    }


    @Override
    public void arrange() {

    }
}
