package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

public abstract class CollapsableContainer extends BaseContainer {

    public boolean isOpen = false;

    public CollapsableContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }

    public abstract Vector2i getClosedSize();
    public abstract Vector2i getOpenSize();


    public void toggle() {
        isOpen = !isOpen;
        forEachInBranch((element, parent, depth) -> {
            element.setActive(!isOpen);
            element.setVisible(!isOpen);
        }, 0);
    }

    public void open() {
        isOpen = true;
        forEachInBranch((element, parent, depth) -> {
            element.setActive(true);
            element.setVisible(true);
        }, 0);
    }

    public void close() {
        isOpen = false;
        forEachInBranch((element, parent, depth) -> {
            element.setActive(false);
            element.setVisible(false);
        }, 0);
    }
    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        renderChildren = isOpen;
        updateChildren = isOpen;
        for (GuiElement element : this) {
            if (renderChildren) {

                element.render(context, mouseX, mouseY, delta);
            }
        }

        super.preTransform(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return isOpen && super.isMouseOver(mouseX, mouseY);
    }

    @Override
    public Vector2i getSize() {
        return (isOpen) ? getOpenSize() : getClosedSize();
    }

    @Override
    public boolean changed() {
        for (GuiElement element : this) {
            if (element.changed()) {
                return true;
            }
        }

        return super.changed();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        for (GuiElement element : this) {

            if (updateChildren) {

                element.mouseMoved(mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (GuiElement element : this) {

            if (updateChildren) {

                element.mouseClicked(mouseX, mouseY, button);
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (GuiElement element : this) {
            if (updateChildren) {

                element.mouseReleased(mouseX, mouseY, button);
            }
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        for (GuiElement element : this) {
            if (updateChildren) {

                element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (GuiElement element : this) {
            if (updateChildren) {
                element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (GuiElement element : this) {
            if (updateChildren) {

                element.keyPressed(keyCode, scanCode, modifiers);
            }
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {

        for (GuiElement element : this) {
            if (updateChildren) {
                element.keyReleased(keyCode, scanCode, modifiers);
            }
        }


        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        for (GuiElement element : this) {
            if (updateChildren) {

                element.charTyped(chr, modifiers);
            }
        }

        return false;
    }


    @Override
    public Property[] getProperties() {
        var open = new Property("Open", "isOpen", "isOpen", Boolean.class);

        return mergeProperties(super.getProperties(), new Property[]{open});
    }
}
