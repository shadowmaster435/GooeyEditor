package org.shadowmaster435.gooeyeditor.screen.elements.container;

import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

public class ListContainer extends BaseContainer {

    protected int listHeight = 0;

    public ListContainer(int x, int y, int w, int h, int element_spacing, boolean editMode) {
        super(x, y, w, h, editMode);
        setElementSpacing(element_spacing);
    }

    @Override
    public void arrange() {
        var y_offset = 0;
        for (GuiElement element : this) {
            element.setX(0);
            element.setY(y_offset );
            y_offset += getElementSpacing() + element.getHeight();
        }
        listHeight = y_offset - getElementSpacing();
    }

    public int getListHeight() {
        return listHeight;
    }

    public boolean listHeightExceedsBounds() {
        return listHeight > getHeight();
    }

}
