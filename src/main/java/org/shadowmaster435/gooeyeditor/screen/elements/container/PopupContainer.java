package org.shadowmaster435.gooeyeditor.screen.elements.container;


import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;


/**
 * A {@link CollapsableContainer} that can be opened at a provided position.
 */
public class PopupContainer extends CollapsableContainer {


    public PopupContainer(int w, int h, boolean editMode) {
        super(0, 0, w, h, editMode);
    }

    @Override
    public void arrange() {

    }



    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isOpen) {
            return super.mouseReleased(mouseX, mouseY, button);
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isOpen) {
            return super.mouseClicked(mouseX, mouseY, button);

        } else {
            return false;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (isOpen) {
            return super.isMouseOver(mouseX, mouseY);
        } else {
            return false;
        }
    }

    public void open(Vector2i vec) {
        open(vec.x, vec.y);
        super.open();
    }

    @Override
    public void open() {
        isOpen = true;
        renderChildren = true;
        updateChildren = true;
        super.open();
    }

    public void open(int x, int y) {
        isOpen = true;
        renderChildren = true;
        updateChildren = true;
        setX(x);
        setY(y);
        super.open();
    }

    @Override
    public Vector2i getClosedSize() {
        return new Vector2i(getWidth(), getHeight());
    }

    @Override
    public Vector2i getOpenSize() {
        return new Vector2i(getWidth(), getHeight());
    }

    @Override
    public void close() {
        isOpen = false;
        renderChildren = false;
        updateChildren = false;
    }

    @Override
    public SealedGuiElement getHoveredChild(int mouseX, int mouseY) {
        if (isOpen) {
            return super.getHoveredChild(mouseX, mouseY);
        } else {
            return null;
        }
    }

    @Override
    public boolean isChildHoverable(int mouseX, int mouseY, SealedGuiElement element) {
        if (isOpen) {
            return super.isChildHoverable(mouseX, mouseY, element);
        } else {
            return false;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (isOpen) {
            super.render(context, mouseX, mouseY, delta);
        }
    }

    public boolean isOpen() {
        return isOpen;
    }
}
