package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.ParentableWidgetBase;

/**
 * Base class for containers
 * Any elements added via {@link BaseContainer#addElement(GuiElement)} are will be rendered here.
 */
public abstract class BaseContainer extends ParentableWidgetBase {

    public int element_spacing = 0;

    public BaseContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }


    /**
     * Set element spacing. <p>
     * In base class for convenience, does nothing on its own.
     */
    public void setElementSpacing(int element_spacing) {
        this.element_spacing = element_spacing;
    }

    /**
     * Get element spacing. <p>
     * In base class for convenience, does nothing on its own.
     */
    public int getElementSpacing() {
        return element_spacing;
    }

    public void addElement(GuiElement element) {
        widgets.add(element);
    }
    public void removeElement(int index) {
        widgets.remove(index);
    }
    public void removeElement(GuiElement element) {
        widgets.remove(element);
    }

    public boolean isChildHoverable(int mouseX, int mouseY, GuiElement element) {
        return this.isMouseOver(mouseX, mouseY) && element.isMouseOver(mouseX, mouseY);
    }

    @Override
    public GuiElement getHoveredChild(int mouseX, int mouseY) {
        for (GuiElement element : this) {
            if (isChildHoverable(mouseX, mouseY, element)) {
                return element;
            }
        }
        return null;
    }
    public abstract void arrange();

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        setScissor(getX(), getY(), getX() + getWidth(), getY() + getHeight());
        if (changed()) {
            arrange();
        }
        super.preTransform(context, mouseX, mouseY, delta);
        for (GuiElement element : this) {
            element.render(context, mouseX, mouseY, delta);
        }
    }
    @Override
    public Property[] getProperties() {
        var element_spacing = new Property("Element Spacing", "element_spacing", "element_spacing", Integer.class);
        return new Property[]{element_spacing};
    }

}
