package org.shadowmaster435.gooeyeditor.screen.elements.container;

/**
 * Functions like a list container that can resize entries to a set uniform size
 */
public class BoxContainer extends BaseContainer{

    public boolean vertical = true;
    public boolean resizeElements = true;

    public BoxContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }
    public BoxContainer(int x, int y, boolean editMode) {
        super(x, y, 32, 32, editMode);
    }
    @Override
    public void arrange() {
        if (vertical) {
            arrangeVertical();
        } else {
            arrangeHorizontal();
        }
    }

    private void arrangeHorizontal() {
        var elements = getElements();
        for (int i = 0; i < elements.size(); ++i) {
            var s = getElementSize();
            var e = elements.get(i);
            e.setX(s * i);
            e.setY(0);
            if (resizeElements) {
                e.setWidth(s - element_spacing);
                e.setHeight(getHeight());
            }
        }
    }
    private void arrangeVertical() {
        var elements = getElements();
        for (int i = 0; i < elements.size(); ++i) {
            var s = getElementSize();
            var e = elements.get(i);
            e.setX(0);
            e.setY(s * i);
            if (resizeElements) {
                e.setWidth(getWidth());
                e.setHeight(s - element_spacing);
            }
        }
    }

    /**
     * @return Height or Width of all elements in this container.
     */
    public int getElementSize() {
        return ((((vertical) ? getHeight() : getWidth())) + element_spacing) / getElements().size();
    }


    @Override
    public Property[] getProperties() {
        var vertical = new Property("Vertical", "vertical", "vertical", Boolean.class);
        var resizeElements = new Property("Resize Contents", "resizeElements", "resizeElements", Boolean.class);

        return mergeProperties(super.getProperties(), vertical, resizeElements);
    }
}
