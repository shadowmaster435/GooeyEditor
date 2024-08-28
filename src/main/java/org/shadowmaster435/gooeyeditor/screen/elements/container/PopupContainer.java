package org.shadowmaster435.gooeyeditor.screen.elements.container;


import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

public class PopupContainer extends BaseContainer {

    private boolean is_open = false;

    public PopupContainer(int w, int h, boolean editMode) {
        super(0, 0, w, h, editMode);
    }

    @Override
    public void arrange() {

    }



    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (is_open) {
            return super.mouseReleased(mouseX, mouseY, button);
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (is_open) {
            return super.mouseClicked(mouseX, mouseY, button);

        } else {
            return false;
        }
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (is_open) {
            return super.isMouseOver(mouseX, mouseY);
        } else {
            return false;
        }
    }

    public void open(Vector2i vec) {
        open(vec.x, vec.y);
    }


    public void open(int x, int y) {
        is_open = true;
        renderChildren = true;
        updateChildren = true;
        setX(x);
        setY(y);
    }

    public void close() {
        is_open = false;
        renderChildren = false;
        updateChildren = false;
    }

    @Override
    public GuiElement getHoveredChild(int mouseX, int mouseY) {
        if (is_open) {
            return super.getHoveredChild(mouseX, mouseY);
        } else {
            return null;
        }
    }

    @Override
    public boolean isChildHoverable(int mouseX, int mouseY, GuiElement element) {
        if (is_open) {
            return super.isChildHoverable(mouseX, mouseY, element);
        } else {
            return false;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (is_open) {
            super.render(context, mouseX, mouseY, delta);
        }
    }

    public boolean isOpen() {
        return is_open;
    }
}
