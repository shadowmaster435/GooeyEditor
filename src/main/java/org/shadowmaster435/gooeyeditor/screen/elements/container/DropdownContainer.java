package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import org.joml.Vector2i;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;

public class DropdownContainer extends CollapsableContainer {

    public int entry_offset = 0;

    public DropdownContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }

    @Override
    public Vector2i getClosedSize() {
        return getElement(0).getSize();
    }

    @Override
    public Vector2i getOpenSize() {
        return (new Vector2i((int) getChildBounds().getWidth(), (int) getChildBounds().getHeight()));
    }

    @Override
    public int getHeight() {
        int y_offset = 0;

        for (GuiElement element : this) {
            y_offset += getElementSpacing() + element.getHeight();
        }
        return y_offset;
    }

    @Override
    public void arrange() {
        int y_offset = 0;
        boolean first = true;
        for (GuiElement element : this) {
            if (first) {
                element.setX(getX());
                first = false;
            } else {
                element.setX(getX() + entry_offset);
            }
            element.setY(getY() + y_offset);
            y_offset += getElementSpacing() + element.getHeight();
        }

    }
    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        var first = true;
        renderChildren = isOpen;
        updateChildren = isOpen;
        for (GuiElement element : this) {
            if (first) {
                element.render(context, mouseX, mouseY, delta);
                first = false;
            }
            if (renderChildren) {

                element.render(context, mouseX, mouseY, delta);
            }
        }
        super.preTransform(context, mouseX, mouseY, delta);
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
        var first = true;
        for (GuiElement element : this) {
            if (first) {
                element.mouseMoved(mouseX, mouseY);

                first = false;
            }
            if (updateChildren) {

                element.mouseMoved(mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var first = true;
        for (GuiElement element : this) {
            if (first) {
                element.mouseClicked(mouseX, mouseY, button);
                first = false;
            }
            if (updateChildren) {

                element.mouseClicked(mouseX, mouseY, button);
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        var first = true;
        for (GuiElement element : this) {
            if (first) {
                element.mouseReleased(mouseX, mouseY, button);
                first = false;
            }
            if (updateChildren) {

                element.mouseReleased(mouseX, mouseY, button);
            }
        }

        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        var first = true;
        for (GuiElement element : this) {
            if (first) {
                element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);

                first = false;
            }
            if (updateChildren) {

                element.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        var first = true;
        for (GuiElement element : this) {
            if (first) {
                element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

                first = false;
            }
            if (updateChildren) {
                element.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);

            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        var first = true;
        for (GuiElement element : this) {
            if (first) {
                element.keyPressed(keyCode, scanCode, modifiers);

                first = false;
            }
            if (updateChildren) {

                element.keyPressed(keyCode, scanCode, modifiers);
            }
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {

        var first = true;
        for (GuiElement element : this) {
            if (first) {
                element.keyReleased(keyCode, scanCode, modifiers);

                first = false;
            }
            if (updateChildren) {
                element.keyReleased(keyCode, scanCode, modifiers);
            }
        }


        return false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        var first = true;

        for (GuiElement element : this) {
            if (first) {
                element.charTyped(chr, modifiers);

                first = false;
            }
            if (updateChildren) {

                element.charTyped(chr, modifiers);
            }
        }

        return false;
    }

    @Override
    public Property[] getProperties() {
        var entry_offset = new Property("Entry X Offset", "entry_offset", "entry_offset", Integer.class);

        return mergeProperties(super.getProperties(), new Property[]{entry_offset});
    }
}
