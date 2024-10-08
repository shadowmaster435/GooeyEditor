package org.shadowmaster435.gooeyeditor.screen.elements.container;

import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;

public class ListContainer extends BaseContainer {

    protected int listHeight = 0;
    public boolean vertical = true;
    public boolean repositionElements = false;

    public ListContainer(int x, int y, int w, int h, int element_spacing, boolean editMode) {
        super(x, y, w, h, editMode);

        setElementSpacing(element_spacing);
    }

    @Override
    public void arrange() {
        var y_offset = 0;
        for (SealedGuiElement element : this) {
            if (vertical) {
                element.setX((repositionElements) ? -element.getWidth() : 0);
                element.setY(y_offset );
                y_offset += getElementSpacing() + element.getHeight();
            } else {
                element.setX(y_offset);
                element.setY((repositionElements) ? -element.getHeight() : 0);
                y_offset += getElementSpacing() + element.getWidth();
            }
        }
        listHeight = y_offset - getElementSpacing();
    }


    @Override
    public Property[] getProperties() {
        var vertical = new Property("Vertical", "vertical", "vertical", Boolean.class);
        return mergeProperties(super.getProperties(), vertical);
    }

    public int getListHeight() {
        return listHeight;
    }

    public boolean listExceedsBounds() {
        return listHeight > ((vertical) ? getHeight() : getWidth());
    }

}
