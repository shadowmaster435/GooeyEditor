package org.shadowmaster435.gooeyeditor.screen.elements;

import net.minecraft.text.Text;
import org.joml.Vector2i;

import java.util.function.Consumer;

public abstract class GuiButton extends GuiElement {

    public Consumer<GuiButton> pressFunction;
    public boolean pressed = false;
    public boolean toggle_mode = false;


    public GuiButton(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);

    }

    public GuiButton(int x, int y, boolean editMode) {
        super(x, y, editMode);
    }
    public <B extends GuiButton> void setPressFunction(Consumer<GuiButton> func) {
        this.pressFunction = func;
    }

    public Vector2i getSize() {
        return new Vector2i(getWidth(), getHeight());
    }

    @Override
    public Property[] getProperties() {
        var pressed = new Property("Pressed", "pressed", "pressed", Boolean.class);

        var toggle_mode = new Property("Toggle Mode", "toggle_mode", "toggle_mode", Boolean.class);

        return new Property[]{toggle_mode, pressed};
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            setFocused(true);
            if (toggle_mode) {
                pressed = !pressed;
            } else {
                pressed = true;
            }
        }
        if (isMouseOver(mouseX, mouseY) && pressFunction != null) {
            pressFunction.accept(this);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!toggle_mode) {
            pressed = false;
        }
        setFocused(false);

        return super.mouseReleased(mouseX, mouseY, button);
    }

}
